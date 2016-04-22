package com.zenika.liquid.democracy.api.util;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.vote.TooManyPointsException;
import com.zenika.liquid.democracy.api.exception.vote.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.vote.VotePropositionIncorrectException;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;
import com.zenika.liquid.democracy.model.WeightedChoice;

public class VoteUtil {

	public static void checkVotes(Vote vote, Subject s, String userId) throws VotePropositionIncorrectException,
			TooManyPointsException, UserAlreadyVoteException, CloseSubjectException, UserAlreadyGavePowerException {

		if (s.isClosed()) {
			throw new CloseSubjectException();
		}

		Optional<Vote> foundVote = s.findVote(userId);

		if (foundVote.isPresent()) {
			throw new UserAlreadyVoteException();
		}

		Optional<Power> foundPower = s.findPower(userId);

		if (foundPower.isPresent()) {
			throw new UserAlreadyGavePowerException();
		}

		int pointsVoted = vote.getChoices().stream().collect(Collectors.summingInt(WeightedChoice::getPoints));

		if (pointsVoted > s.getMaxPoints()) {
			throw new TooManyPointsException();
		}

		for (WeightedChoice c : vote.getChoices()) {
			Optional<Proposition> propositionFound = s.getPropositions().stream()
					.filter(p -> p.getId().equals(c.getPropositionId())).findFirst();

			propositionFound.orElseThrow(VotePropositionIncorrectException::new);

			Stream<WeightedChoice> propositionsFound = vote.getChoices().stream()
					.filter(cTmp -> cTmp.getPropositionId().equals(c.getPropositionId()));

			if (propositionsFound.count() != 1) {
				throw new VotePropositionIncorrectException();
			}
		}

	}

	public static void prepareVotes(Vote vote, Subject s, String userId) {
		Long power = s.getPowers().stream().filter(p -> p.getCollaboratorIdTo().equals(userId)).count();

		for (WeightedChoice c : vote.getChoices()) {
			c.setPoints(c.getPoints() * (1 + power.intValue()));
		}

		vote.setCollaboratorId(userId);

		s.getVotes().add(vote);

		compileResults(s);
	}

	private static void compileResults(Subject s) {
		for (Proposition p : s.getPropositions()) {
			p.setPoints(0);
			s.getVotes().stream().forEach(v -> {
				int nbPointsVoted = v.getChoices().stream().filter(c -> {
					return p.getId().equals(c.getPropositionId());
				}).collect(Collectors.summingInt(WeightedChoice::getPoints));
				p.setPoints(nbPointsVoted + p.getPoints());
			});
		}
	}
}
