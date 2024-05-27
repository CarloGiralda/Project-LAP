package com.notificationservice.notificationservice.MQListener;

import com.notificationservice.notificationservice.dto.CarMessage;
import com.notificationservice.notificationservice.dto.CarSubscriptionMessage;
import com.notificationservice.notificationservice.service.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Component
public class CarSubscriptionIdsListener {

    private WebSocketService webSocketService;


    @RabbitListener(queues = "car_subscription_queue", id="listener")
    public void listener(CarSubscriptionMessage message){

        log.info("received ids from queue: {}", Arrays.toString(message.getUserIds()));

        CarMessage carMessage = new CarMessage(message.getCarId());

        webSocketService.notifySubscribedUser(Arrays.asList(message.getUserIds()), carMessage);
    }


}
