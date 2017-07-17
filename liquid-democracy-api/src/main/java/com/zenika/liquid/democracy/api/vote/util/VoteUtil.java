package com.zenika.liquid.democracy.api.vote.util;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.vote.exception.TooManyPointsException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.vote.exception.VotePropositionIncorrectException;
import com.zenika.liquid.democracy.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class VoteUtil {

    public static void checkVotes(final Vote vote, final Subject subject, final String userId) {
        if (subject.isClosed()) {
            throw new CloseSubjectException();
        }

        final Optional<Vote> foundVote = subject.findVote(userId);
        if (foundVote.isPresent()) {
            throw new UserAlreadyVoteException();
        }

        final Optional<Power> foundPower = subject.findPower(userId);
        if (foundPower.isPresent()) {
            throw new UserAlreadyGavePowerException();
        }

        final int pointsVoted = vote.getChoices().stream()
                .mapToInt(WeightedChoice::getPoints)
                .sum();
        if (pointsVoted > subject.getMaxPoints()) {
            throw new TooManyPointsException();
        }

        for (WeightedChoice c : vote.getChoices()) {
            final Optional<Proposition> propositionFound = subject.getPropositions().stream()
                    .filter(p -> p.getId().equals(c.getPropositionId()))
                    .findFirst();

            propositionFound.orElseThrow(VotePropositionIncorrectException::new);

            final long count = vote.getChoices().stream()
                    .filter(cTmp -> cTmp.getPropositionId().equals(c.getPropositionId()))
                    .count();

            if (count != 1) {
                throw new VotePropositionIncorrectException();
            }
        }

    }

    public static void prepareVotes(final Vote vote, final Subject subject, final String userId) {
        final List<Vote> votes = new ArrayList<>();

        final Deque<String> users = new ArrayDeque<>();
        users.add(userId);

        while (!users.isEmpty()) {
            final String user = users.pop();

            final List<String> dependencies = subject.getPowers().stream()
                    .filter(p -> p.getCollaboratorIdTo().equals(user))
                    .map(Power::getCollaboratorIdFrom)
                    .collect(Collectors.toList());

            votes.addAll(
                    dependencies.stream().map(d -> {
                        final Vote v = new Vote();
                        v.setCollaboratorId(d);
                        v.setChoices(vote.getChoices());
                        return v;
                    })
                            .collect(Collectors.toList())
            );

            users.addAll(dependencies);
        }

        vote.setCollaboratorId(userId);

        subject.getVotes().add(vote);
        subject.getVotes().addAll(votes);

        compileResults(subject);
    }

    public static void compileResults(final Subject s) {
        for (Proposition p : s.getPropositions()) {
            p.setPoints(0);
            s.getVotes().forEach(v -> {
                final int nbPointsVoted = v.getChoices().stream()
                        .filter(c -> p.getId().equals(c.getPropositionId()))
                        .mapToInt(WeightedChoice::getPoints).sum();
                p.setPoints(nbPointsVoted + p.getPoints());
            });
        }
    }
}
