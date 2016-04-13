package com.zenika.liquid.democracy.model;

public class WeightedChoice {

	private int points;

	private String propositionId;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String toString() {
		StringBuilder strB = new StringBuilder();

		strB.append("Choice : ");
		strB.append("\n \t Proposition : ").append(getPropositionId());
		strB.append("\n \t Points : ").append(getPoints());

		return strB.toString();
	}

	public String getPropositionId() {
		return propositionId;
	}

	public void setPropositionId(String propositionId) {
		this.propositionId = propositionId;
	}

}
