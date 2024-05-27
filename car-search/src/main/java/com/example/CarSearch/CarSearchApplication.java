package com.example.CarSearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CarSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarSearchApplication.class, args);
	}

}
