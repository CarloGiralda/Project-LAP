package com.example.CarInsertion.config;

import com.example.CarInsertion.repository.CarOfferUtRepo;
import com.example.CarInsertion.repository.CarOfferUtRepoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public CarOfferUtRepo insertRepo() {
        return new CarOfferUtRepoImpl();
    }

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }

}
