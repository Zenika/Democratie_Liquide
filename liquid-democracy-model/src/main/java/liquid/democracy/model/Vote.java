package liquid.democracy.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Vote {

	@Id
	private ObjectId id;

	private Long collaborateurId;

	private List<WeightedChoice> choices;

	public Vote() {
		id = new ObjectId();
		choices = new ArrayList<>();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Long getCollaborateurId() {
		return collaborateurId;
	}

	public void setCollaborateurId(Long collaborateurId) {
		this.collaborateurId = collaborateurId;
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
		strB.append("\n \t CollaborateurId : ").append(getCollaborateurId());

		for (WeightedChoice choice : choices) {
			strB.append("\n \t").append(choice);
		}

		return strB.toString();
	}

}
