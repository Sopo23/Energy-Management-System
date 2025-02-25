package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example")
public class MonitoringNComunicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringNComunicationApplication.class, args);
	}

}
