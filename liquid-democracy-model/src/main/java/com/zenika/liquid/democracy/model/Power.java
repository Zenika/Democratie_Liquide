package com.zenika.liquid.democracy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Power {

	@Id
	private String id;

	private String collaboratorIdFrom;

	private String collaboratorIdTo;

	public Power() {
		id = new ObjectId().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCollaboratorIdFrom() {
		return collaboratorIdFrom;
	}

	public void setCollaboratorIdFrom(String collaboratorIdFrom) {
		this.collaboratorIdFrom = collaboratorIdFrom;
	}

	public String getCollaboratorIdTo() {
		return collaboratorIdTo;
	}

	public void setCollaboratorIdTo(String collaboratorIdTo) {
		this.collaboratorIdTo = collaboratorIdTo;
	}

}
