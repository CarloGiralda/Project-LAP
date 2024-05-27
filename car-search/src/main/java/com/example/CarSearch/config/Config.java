package com.example.CarSearch.config;

import com.example.CarSearch.repository.SearchRepo;
import com.example.CarSearch.repository.SearchRepoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public SearchRepo searchRepo() {
        return new SearchRepoImpl();
    }

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }
}
