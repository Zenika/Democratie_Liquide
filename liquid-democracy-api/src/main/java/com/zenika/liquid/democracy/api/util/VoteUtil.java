package com.zenika.liquid.democracy.api.util;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zenika.liquid.democracy.api.exception.TooManyPointsException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.VotePropositionIncorrectException;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;
import com.zenika.liquid.democracy.model.WeightedChoice;

public class VoteUtil {

	public static void checkVotes(Vote vote, Subject s, String userId)
			throws VotePropositionIncorrectException, TooManyPointsException, UserAlreadyVoteException {

		Long nbVoteForUser = s.getVotes().stream().filter(v -> v.getCollaborateurId().equals(userId)).count();

		if (nbVoteForUser > 0) {
			throw new UserAlreadyVoteException();
		}

		int pointsVoted = vote.getChoices().stream().collect(Collectors.summingInt(WeightedChoice::getPoints));

		if (pointsVoted > s.getMaxPoints()) {
			throw new TooManyPointsException();
		}

		for (WeightedChoice c : vote.getChoices()) {
			Optional<Proposition> propositionFound = s.getPropositions().stream()
					.filter(p -> p.getId().equals(c.getProposition().getId())).findFirst();

			propositionFound.orElseThrow(VotePropositionIncorrectException::new);

			Stream<WeightedChoice> propositionsFound = vote.getChoices().stream()
					.filter(cTmp -> cTmp.getProposition().getId().equals(c.getProposition().getId()));

			if (propositionsFound.count() != 1) {
				throw new VotePropositionIncorrectException();
			}
		}

	}

	public static void prepareVotes(Vote vote, Subject s, String userId) {
		for (WeightedChoice c : vote.getChoices()) {
			Optional<Proposition> propositionFound = s.getPropositions().stream()
					.filter(p -> p.getId().equals(c.getProposition().getId())).findFirst();
			propositionFound.ifPresent(p -> {
				c.setProposition(p);
			});
		}

		vote.setCollaborateurId(userId);

		s.getVotes().add(vote);
	}
}
