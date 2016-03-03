package com.zenika.liquid.democracy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Power {

	@Id
	private ObjectId id;

	private String collaborateurIdFrom;

	private String collaborateurIdTo;

	public Power() {
		id = new ObjectId();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
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
