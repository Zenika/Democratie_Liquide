package com.zenika.liquid.democracy.api.vote.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.vote.exception.TooManyPointsException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.vote.exception.VotePropositionIncorrectException;
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
		List<Vote> votes = s.getPowers().stream().filter(p -> p.getCollaboratorIdTo().equals(userId)).map(power -> {
			Vote v = new Vote();
			v.setCollaboratorId(power.getCollaboratorIdFrom());
			v.setChoices(vote.getChoices());
			return v;
		}).collect(Collectors.toList());

		vote.setCollaboratorId(userId);

		s.getVotes().add(vote);
		s.getVotes().addAll(votes);

		compileResults(s);
	}

	public static void compileResults(Subject s) {
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
