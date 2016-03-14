package com.zenika.liquid.democracy.api;

import org.springframework.context.annotation.Configuration;

import com.zenika.si.core.zenika.authentication.service.CollaboratorService;
import com.zenika.si.core.zenika.model.Collaborator;

@Configuration
public class CollaboratorServiceStub extends CollaboratorService {

	public Collaborator currentUser() {
		Collaborator c = new Collaborator();
		c.setEmail("sandra.parlant@zenika.com");
		c.setCollaboratorId("sandra.parlant@zenika.com");
		return c;
	}
}
