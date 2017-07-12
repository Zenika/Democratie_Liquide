package com.zenika.liquid.democracy.authentication.service;

import com.zenika.liquid.democracy.authentication.persistence.CollaboratorRepository;
import com.zenika.si.core.zenika.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;

public class CollaboratorServiceStub extends CollaboratorService {

	@Autowired
	public CollaboratorServiceStub(CollaboratorRepository collaboratorRepository) {
		super(collaboratorRepository);
	}

	public Collaborator currentUser() {
		Collaborator c = new Collaborator();
		c.setEmail("sandra.parlant@zenika.com");
		c.setCollaboratorId("sandra.parlant@zenika.com");
		return c;
	}
}
