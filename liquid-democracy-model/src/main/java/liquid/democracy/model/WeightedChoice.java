package liquid.democracy.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class WeightedChoice {

	private int points;

	@DBRef
	Proposition proposition;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Proposition getProposition() {
		return proposition;
	}

	public void setProposition(Proposition proposition) {
		this.proposition = proposition;
	}

	@Override
	public String toString() {
		StringBuilder strB = new StringBuilder();

		strB.append("Choice : ");
		strB.append("\n \t Proposition : ").append(getProposition());
		strB.append("\n \t Points : ").append(getPoints());

		return strB.toString();
	}

}
