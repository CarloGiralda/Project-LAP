package com.notificationservice.notificationservice.config;

import com.notificationservice.notificationservice.Handler.UserHandshakeHandler;
import com.notificationservice.notificationservice.service.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserUtil userUtil;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/ws");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/websocket")
                .setHandshakeHandler(new UserHandshakeHandler(userUtil))
                .setAllowedOrigins("http://localhost:8080")
                .setAllowedOrigins("http://localhost:8081");
        registry.addEndpoint("/sockjs")
                .setHandshakeHandler(new UserHandshakeHandler(userUtil))
                .setAllowedOrigins("http://localhost:8080")
                .setAllowedOrigins("http://localhost:8081")
                .withSockJS();

    }



}
