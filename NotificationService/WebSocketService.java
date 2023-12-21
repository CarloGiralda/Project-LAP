package com.notificationservice.notificationservice.service;



import com.notificationservice.notificationservice.dto.CarAvailableNotification;
import com.notificationservice.notificationservice.dto.CarMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class WebSocketService {

    private SimpMessagingTemplate messagingTemplate;

    private final RestTemplate restTemplate;

    private final DiscoveryClientService discoveryClientService;
    private static final String frontendUrl = "http://localhost:8081";

    public void notifySubscribedUser(List<String> userId, CarMessage carMessage){


        String searchServiceUrl = discoveryClientService.getServiceUrl("CARSEARCH-SERVICE") + "/getCarNameById/" + carMessage.getCarId();
        ResponseEntity<String> response = restTemplate.getForEntity(searchServiceUrl, String.class);

        if(response.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException("cannot get the car name from search service");
        }
        String carName = response.getBody();

        CarAvailableNotification carNotification = new CarAvailableNotification(carMessage.getCarId(), carName,frontendUrl+"/carsearch/" + carMessage.getCarId());

        for (String id: userId){
            messagingTemplate.convertAndSendToUser(
                    id,"/topic/car-notification", carNotification);
            log.info("notification sent to user {}", id);
        }

    }


}
