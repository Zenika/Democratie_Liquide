package com.zenika.liquid.democracy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zenika.liquid.democracy.authentication.service.CollaboratorService;

@Configuration
@Profile({ "docker", "test-prod" })
public class ProdConfiguration {

	@Bean
	public CollaboratorService collaboratorService() {
		return new CollaboratorService();
	}

}
