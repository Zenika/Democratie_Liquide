package com.zenika.liquid.democracy.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Category {

	@Id
	private String uuid;

	@Version
	private String version;

	private String title;

	private String description;

	private List<Power> powers;

	public Category() {
		powers = new ArrayList<>();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public List<Power> getPowers() {
		return powers;
	}

	public void setPowers(List<Power> powers) {
		this.powers = powers;
	}

	public Optional<Power> findPower(String userId) {
		Optional<Power> foundPower = getPowers().stream().filter(p -> {
			return userId.equals(p.getCollaboratorIdFrom());
		}).findFirst();

		return foundPower;
	}

}
