package com.example.apigw.filter;

import com.example.apigw.discoveryclient.DiscoveryClientService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.Objects;
import org.slf4j.Logger;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);


    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private DiscoveryClientService discoveryClientService;

    @Autowired
    private RestTemplate restTemplate;



    public AuthenticationFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();



            if(routeValidator.isSecured.test(exchange.getRequest())){

                log.info("extracting authentication header");
                //check if header contains header
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("missing authorization error");
                }
                // if jwt token is in the HTTP header get it
                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                log.info("extracted authentication header: {}", authHeader);
                if (authHeader.startsWith("Bearer")){
                    authHeader = authHeader.substring(7);
                }
                try {

                    // Get service url through eureka client
                    String userServiceUrl = discoveryClientService.getServiceUrl("USER-SERVICE");
                    String jwtValidationUrl = userServiceUrl + "/auth/validate?token=" + authHeader;
                    log.info("contacting user-service for jwt token validation at {}", jwtValidationUrl);

                    // REST call to AUTH service, it is known through service discovery eureka server
                    ResponseEntity<String> responseJwtValidation = restTemplate.getForEntity(jwtValidationUrl, String.class);


                    if(responseJwtValidation.getStatusCode() != HttpStatus.OK){
                        log.info("jwt token not valid");
                        throw new RuntimeException("jwt token not valid");
                    }
                    log.info("jwt validation response: {}", responseJwtValidation.getBody());

                    // interact with extract username API
                    String extractUsernameFromJwtUrl = userServiceUrl + "/auth/extractUsernameFromJwt?token=" + authHeader;
                    ResponseEntity<String> extractUsernameResponse = restTemplate.getForEntity(extractUsernameFromJwtUrl, String.class);

                    if(extractUsernameResponse.getStatusCode() != HttpStatus.OK){
                        log.info("cannot extract username from jwt token");
                        throw new RuntimeException("cannot extract username from jwt token");
                    }
                    String responseBody = extractUsernameResponse.getBody();

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);

                    // Extract the username
                    String username = jsonNode.get("username").asText();

                    // Now you can use the 'username' variable as needed
                    log.info("extracted username: {}", username);


                    // add a new header to say that that user is authenticated, every microservice should include it
                    request = exchange.getRequest()
                            .mutate()
                            .header("Logged-In-User", username)
                            .build();
                    log.info("sending request with header: {}", request.getHeaders());

                } catch (Exception e){
                    // CUSTOMIZE with
                    throw new RuntimeException("unauthorized access to application " + e.getMessage());
                }


            }

            return chain.filter(exchange.mutate().request(request).build());
        });
    }


    public static class Config {

    }

}
