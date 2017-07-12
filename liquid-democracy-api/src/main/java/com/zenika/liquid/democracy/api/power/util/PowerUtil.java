package com.zenika.liquid.democracy.api.power.util;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.CircularPowerDependencyException;
import com.zenika.liquid.democracy.api.power.exception.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.vote.util.VoteUtil;
import com.zenika.liquid.democracy.model.Category;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;

import java.util.*;
import java.util.stream.Collectors;

public class PowerUtil {

    public static void checkPowerForAddition(final Power power, final Subject subject, final String userId) {
        // On ne peut pas déléguer un sujet déjà clos
        if (subject.isClosed()) {
            throw new CloseSubjectException();
        }

        // On ne peut pas déléguer si on a déjà voté
        final Optional<Vote> foundVote = subject.findVote(userId);
        if (foundVote.isPresent()) {
            throw new UserAlreadyVoteException();
        }

        // On ne peut pas déléguer plusieurs fois
        final Optional<Power> foundPower = subject.getPowers().stream()
                .filter(p -> userId.equals(p.getCollaboratorIdFrom()))
                .findFirst();
        if (foundPower.isPresent()) {
            throw new UserAlreadyGavePowerException();
        }

        // On vérifie que le fait de déléguer ne crée pas de dépendance circulaire
        checkCircularDependency(power, subject.getPowers(), userId);
    }

    public static void checkCategoryPowerForAddition(final Power power, final Category category, final String userId) {
        // On ne peut pas déléguer plusieurs fois
        final Optional<Power> foundPower = category.getPowers().stream()
                .filter(p -> userId.equals(p.getCollaboratorIdFrom()))
                .findFirst();
        if (foundPower.isPresent()) {
            throw new UserAlreadyGavePowerException();
        }

        // On vérifie que le fait de déléguer ne crée pas de dépendance circulaire
        checkCircularDependency(power, category.getPowers(), userId);
    }

    public static void preparePower(final Power power, final Subject subject, final String userId) {
        power.setCollaboratorIdFrom(userId);

        subject.findVote(power.getCollaboratorIdTo())
                .ifPresent(vote -> {
                    Vote newVote = new Vote();
                    newVote.setCollaboratorId(userId);
                    newVote.setChoices(vote.getChoices());
                    subject.getVotes().add(newVote);
                });

        subject.getPowers().add(power);

        VoteUtil.compileResults(subject);
    }

    public static void prepareCategoryPower(final Power power, final Category c, final String userId) {
        power.setCollaboratorIdFrom(userId);
        c.getPowers().add(power);
    }

    public static Power checkPowerForDelete(final Subject subject, final String userId) {
        if (subject.isClosed()) {
            throw new CloseSubjectException();
        }

        Optional<Power> power = subject.findPower(userId);
        if (!power.isPresent()) {
            throw new DeleteNonExistingPowerException();
        }

        Optional<Vote> foundVote = subject.findVote(power.get().getCollaboratorIdTo());
        if (foundVote.isPresent()) {
            throw new UserAlreadyVoteException();
        }

        return power.get();
    }

    public static Power checkCategoryPowerForDelete(final Category category, final String userId) {
        Optional<Power> power = category.findPower(userId);
        if (!power.isPresent()) {
            throw new DeleteNonExistingPowerException();
        }

        return power.get();
    }


    private static void checkCircularDependency(final Power power, final List<Power> powers, final String userId) {
        final Deque<Power> powerToCheck = new ArrayDeque<>();
        powerToCheck.add(power);

        while (!powerToCheck.isEmpty()) {
            final Power currentPower = powerToCheck.pop();

            if (Objects.equals(currentPower.getCollaboratorIdTo(), userId)) {
                throw new CircularPowerDependencyException();
            }

            powerToCheck.addAll(
                    powers.stream()
                            .filter(p -> currentPower.getCollaboratorIdTo().equals(p.getCollaboratorIdFrom()))
                            .collect(Collectors.toList())
            );
        }
    }
}
