package com.example.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.services.feignclient") 
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
