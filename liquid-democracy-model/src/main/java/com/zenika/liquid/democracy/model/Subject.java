package com.zenika.liquid.democracy.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Subject {

	@Id
	private String uuid;

	@Version
	private String version;

	private String title;

	private String description;

	private int maxPoints;

	private Date deadLine;

	private Date submitDate;

	private String collaborateurId;

	private List<Proposition> propositions;

	private List<Vote> votes;

	private List<Power> powers;

	@DBRef
	private Category category;

	public Subject() {
		maxPoints = 1;
		propositions = new ArrayList<>();
		powers = new ArrayList<>();
		votes = new ArrayList<>();
	}

	public String getUuid() {
		return uuid;
	}

	public void setId(String uuid) {
		this.uuid = uuid;
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

	public int getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getCollaborateurId() {
		return collaborateurId;
	}

	public void setCollaborateurId(String collaborateurId) {
		this.collaborateurId = collaborateurId;
	}

	public List<Proposition> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<Proposition> propositions) {
		this.propositions = propositions;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public List<Power> getPowers() {
		return powers;
	}

	public void setPowers(List<Power> powers) {
		this.powers = powers;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Optional<Power> findPower(String userId) {
		Optional<Power> foundPower = getPowers().stream().filter(p -> {
			return userId.equals(p.getCollaborateurIdFrom());
		}).findFirst();

		return foundPower;
	}

	public Optional<Vote> findVote(String userId) {
		return getVotes().stream().filter(v -> v.getCollaborateurId().equals(userId)).findFirst();
	}

	public void removePower(Power p) {
		powers.remove(p);

	}

	@JsonIgnore
	public boolean isClosed() {
		Date today = new Date();
		return getDeadLine() != null && getDeadLine().before(today);
	}

	@JsonIgnore
	public boolean isWellFormed() {
		boolean result = !StringUtils.isEmpty(title) && !StringUtils.isEmpty(description);

		result = result && propositions.size() >= 2;

		for (Proposition proposition : propositions) {
			result = result && proposition.isWellFormed();
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder strB = new StringBuilder();

		strB.append("Subject : ").append(getTitle());
		strB.append("\n \t Id : ").append(getUuid());
		strB.append("\n \t Description : ").append(getDescription());
		strB.append("\n \t MaxPoints : ").append(getMaxPoints());
		strB.append("\n \t SubmitDate : ").append(getSubmitDate());
		strB.append("\n \t CollaborateurId : ").append(getCollaborateurId());

		for (Proposition proposition : propositions) {
			strB.append("\n \t").append(proposition);
		}

		for (Power power : powers) {
			strB.append("\n \t").append(power);
		}

		for (Vote vote : votes) {
			strB.append("\n \t").append(vote);
		}

		return strB.toString();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
