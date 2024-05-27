package com.example.CarRating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CarRatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRatingApplication.class, args);
	}

}
