package liquid.democracy.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Vote {
	
	private int points;
	
	private Long collaborateurId;
	
	@DBRef
	Proposition proposition;
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Long getCollaborateurId() {
		return collaborateurId;
	}

	public void setCollaborateurId(Long collaborateurId) {
		this.collaborateurId = collaborateurId;
	}

}
