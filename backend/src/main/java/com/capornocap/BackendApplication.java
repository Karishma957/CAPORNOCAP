package com.capornocap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		System.out.println("Backend application started successfully.");
		System.out.println(
				"Visit http://backend:8080/swagger-ui/index.html#/ to access the swagger for the application.");
	}
}
