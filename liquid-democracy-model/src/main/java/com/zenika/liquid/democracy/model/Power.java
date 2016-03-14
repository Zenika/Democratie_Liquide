package com.zenika.liquid.democracy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Power {

	@Id
	private String id;

	private String collaborateurIdFrom;

	private String collaborateurIdTo;

	public Power() {
		id = new ObjectId().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCollaborateurIdFrom() {
		return collaborateurIdFrom;
	}

	public void setCollaborateurIdFrom(String collaborateurIdFrom) {
		this.collaborateurIdFrom = collaborateurIdFrom;
	}

	public String getCollaborateurIdTo() {
		return collaborateurIdTo;
	}

	public void setCollaborateurIdTo(String collaborateurIdTo) {
		this.collaborateurIdTo = collaborateurIdTo;
	}

}
