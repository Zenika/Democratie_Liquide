package com.zenika.liquid.democracy.model;

import com.zenika.si.core.zenika.model.Collaborator;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document
public class Channel {

	@Id
	private String uuid;

	@Version
	private String version;

	private String title;

	private String description;

	@DBRef
	private List<Collaborator> collaborators;

	public Channel() {
		collaborators = new ArrayList<>();
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

	public List<Collaborator> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<Collaborator> collaborators) {
		this.collaborators = collaborators;
	}

	public Optional<Collaborator> findCollaborator(String userId) {
		return getCollaborators().stream().filter(p -> {
			return userId.equals(p.getCollaboratorId());
		}).findFirst();
	}

	public void removeCollaborator(Collaborator collaborator) {
		collaborators.remove(collaborator);
	}

}
