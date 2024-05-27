package com.example.carbook;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableAsync
@EnableScheduling
@EnableRabbit
public class CarBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarBookApplication.class, args);
	}

}
