package com.zenika.liquid.democracy.api.util;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zenika.liquid.democracy.api.exception.TooManyPointsException;
import com.zenika.liquid.democracy.api.exception.VotePropositionIncorrectException;

import liquid.democracy.model.Proposition;
import liquid.democracy.model.Subject;
import liquid.democracy.model.Vote;
import liquid.democracy.model.WeightedChoice;

public class VoteUtil {

	public static void checkVotes(Vote vote, Subject s)
			throws VotePropositionIncorrectException, TooManyPointsException {

		int pointsVoted = vote.getChoices().stream().collect(Collectors.summingInt(WeightedChoice::getPoints));

		if (pointsVoted > s.getMaxPoints()) {
			throw new TooManyPointsException();
		}

		for (WeightedChoice c : vote.getChoices()) {
			Optional<Proposition> propositionFound = s.getPropositions().stream()
					.filter(p -> p.getTitle() == c.getProposition().getTitle()).findFirst();

			propositionFound.orElseThrow(VotePropositionIncorrectException::new);

			Stream<WeightedChoice> propositionsFound = vote.getChoices().stream()
					.filter(cTmp -> cTmp.getProposition().getTitle() == c.getProposition().getTitle());

			if (propositionsFound.count() != 1) {
				throw new VotePropositionIncorrectException();
			}
		}

	}

	public static void prepareVotes(Vote vote, Subject s) {
		for (WeightedChoice c : vote.getChoices()) {
			Optional<Proposition> propositionFound = s.getPropositions().stream()
					.filter(p -> p.getTitle() == c.getProposition().getTitle()).findFirst();
			propositionFound.ifPresent(p -> {
				c.setProposition(p);
			});
		}

		s.setVote(vote);
	}
}
