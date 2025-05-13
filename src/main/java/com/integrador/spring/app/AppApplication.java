package com.integrador.spring.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Anotación que indica que esta es una aplicación Spring Boot
@SpringBootApplication
public class AppApplication {

	
	// Método principal que inicia la aplicación Spring Boot
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
