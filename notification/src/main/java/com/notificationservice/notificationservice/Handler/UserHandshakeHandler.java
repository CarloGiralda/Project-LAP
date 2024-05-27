package com.notificationservice.notificationservice.Handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.notificationservice.notificationservice.service.UserUtil;
import com.sun.security.auth.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;


@Slf4j
@AllArgsConstructor
@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    private UserUtil userUtil;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {


        String loggedInUser = request.getURI().getQuery().replace("user=", "");

        // TODO check if really required, is it better to keep email or id?
        String userId = null;
/*
        try {
            userId = userUtil.getUserId(loggedInUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("user with id logged: {}", userId);
*/


        return new UserPrincipal(loggedInUser);

    }

}
