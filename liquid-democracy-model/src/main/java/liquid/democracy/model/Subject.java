package liquid.democracy.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

@Document
public class Subject {

	@Id
	private String uuid;

	private String title;

	private String description;

	private int maxPoints;

	private Date deadLine;

	private Date submitDate;

	private Long collaborateurId;

	private List<Proposition> propositions;

	private Vote vote;

	private List<Power> powers;

	public Subject() {
		maxPoints = 1;
		propositions = new ArrayList<>();
		powers = new ArrayList<>();
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

	public Long getCollaborateurId() {
		return collaborateurId;
	}

	public void setCollaborateurId(Long collaborateurId) {
		this.collaborateurId = collaborateurId;
	}

	public List<Proposition> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<Proposition> propositions) {
		this.propositions = propositions;
	}

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	public List<Power> getPowers() {
		return powers;
	}

	public void setPowers(List<Power> powers) {
		this.powers = powers;
	}

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

		strB.append("\n \t").append(vote);

		return strB.toString();
	}

}
