package com.example.demo.config;

import com.example.demo.dto.PaymentDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }


}
