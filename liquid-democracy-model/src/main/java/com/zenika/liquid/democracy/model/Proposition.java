package com.zenika.liquid.democracy.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Proposition {

	@Id
	private String id;

	private String title;

	private String description;

	private int points;

	public Proposition() {
		id = new ObjectId().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public boolean isWellFormed() {
		boolean result = !StringUtils.isEmpty(title);

		return result;
	}

	@Override
	public String toString() {
		StringBuilder strB = new StringBuilder();

		strB.append("Proposition : ").append(getTitle());
		strB.append("\n \t Id : ").append(getId());
		strB.append("\n \t Title : ").append(getTitle());
		strB.append("\n \t Description : ").append(getDescription());

		return strB.toString();
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
