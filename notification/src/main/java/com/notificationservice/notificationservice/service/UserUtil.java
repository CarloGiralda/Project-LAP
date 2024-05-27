package com.notificationservice.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.notificationservice.JsonModel.UserData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Component
@Slf4j
public class UserUtil {


    private final DiscoveryClientService discoveryClientService;


    private final RestTemplate restTemplate;

    public String getUserId(String username) throws JsonProcessingException {

        String userServiceUrl = discoveryClientService.getServiceUrl("USER-SERVICE");
        String extractIdFromUsernameUrl = userServiceUrl + "/auth/getUserIdFromUsername?username=" + username;

        ResponseEntity<String> response = restTemplate.getForEntity(extractIdFromUsernameUrl, String.class);

        if(response.getStatusCode() != HttpStatus.OK){
            log.info("cannot extract id from username");
            throw new RuntimeException("cannot extract username from jwt token");
        }

        // map the json inside the body to get the value
        String json = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        UserData userData = objectMapper.readValue(json, UserData.class);

        return userData.getUsername();


    }


}
