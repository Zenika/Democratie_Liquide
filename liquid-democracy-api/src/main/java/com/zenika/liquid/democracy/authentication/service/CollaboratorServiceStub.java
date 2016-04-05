package com.zenika.liquid.democracy.authentication.service;

import com.zenika.si.core.zenika.model.Collaborator;

public class CollaboratorServiceStub extends CollaboratorService {

	public Collaborator currentUser() {
		Collaborator c = new Collaborator();
		c.setEmail("sandra.parlant@zenika.com");
		c.setCollaboratorId("sandra.parlant@zenika.com");
		return c;
	}
}
