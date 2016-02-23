package liquid.democracy.model;

import java.sql.Date;
import java.util.ArrayList;
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

	private List<Vote> votes;

	private List<Power> powers;

	public Subject() {
		maxPoints = 1;
		propositions = new ArrayList<>();
		votes = new ArrayList<>();
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

	public boolean isWellFormed() {
		boolean result = !StringUtils.isEmpty(title) && !StringUtils.isEmpty(description);

		result = result && propositions.size() >= 2;

		for (Proposition proposition : propositions) {
			result = result && proposition.isWellFormed();
		}

		return result;
	}

}
