package com.zenika.liquid.democracy.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Vote {

	@Id
	private String id;

	private String collaboratorId;

	private List<WeightedChoice> choices;

	public Vote() {
		id = new ObjectId().toString();
		choices = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCollaboratorId() {
		return collaboratorId;
	}

	public void setCollaboratorId(String collaboratorId) {
		this.collaboratorId = collaboratorId;
	}

	public List<WeightedChoice> getChoices() {
		return choices;
	}

	public void setChoices(List<WeightedChoice> choices) {
		this.choices = choices;
	}

	@Override
	public String toString() {
		StringBuilder strB = new StringBuilder();

		strB.append("Vote : ");
		strB.append("\n \t CollaboratorId : ").append(getCollaboratorId());

		for (WeightedChoice choice : choices) {
			strB.append("\n \t").append(choice);
		}

		return strB.toString();
	}

}
