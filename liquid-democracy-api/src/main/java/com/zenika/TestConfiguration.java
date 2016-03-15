package com.zenika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.zenika.si.core.zenika.authentication.service.CollaboratorService;
import com.zenika.si.core.zenika.authentication.service.CollaboratorServiceStub;

@SpringBootApplication
public class TestConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CollaboratorService collaboratorService() {
		return new CollaboratorServiceStub();
	}

}
