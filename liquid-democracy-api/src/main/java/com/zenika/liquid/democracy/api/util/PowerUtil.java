package com.zenika.liquid.democracy.api.util;

import java.util.Optional;

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.power.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.power.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.exception.vote.UserAlreadyVoteException;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;

public class PowerUtil {

	public static void checkPowerForAddition(Power power, Subject subject, String userId)
			throws UserAlreadyGavePowerException, UserGivePowerToHimselfException, CloseSubjectException,
			UserAlreadyVoteException {

		if (subject.isClosed()) {
			throw new CloseSubjectException();
		}

		Optional<Power> foundPower = subject.getPowers().stream().filter(p -> {
			return userId.equals(p.getCollaboratorIdFrom());
		}).findFirst();

		if (foundPower.isPresent()) {
			throw new UserAlreadyGavePowerException();
		}
		
		foundPower = subject.getPowers().stream().filter(p -> {
			return power.getCollaboratorIdTo().equals(p.getCollaboratorIdFrom());
		}).findFirst();

		if (foundPower.isPresent()) {
			throw new UserAlreadyGavePowerException();
		}

		if (userId.equals(power.getCollaboratorIdTo())) {
			throw new UserGivePowerToHimselfException();
		}

		Optional<Vote> foundVote = subject.findVote(userId);
		if (foundVote.isPresent()) {
			throw new UserAlreadyVoteException();
		}

		foundVote = subject.findVote(power.getCollaboratorIdTo());
		if (foundVote.isPresent()) {
			throw new UserAlreadyVoteException();
		}

	}

	public static void preparePower(Power power, Subject s, String userId) {
		power.setCollaboratorIdFrom(userId);
		s.getPowers().add(power);
	}

	public static Power checkPowerForDelete(Subject subject, String userId)
			throws CloseSubjectException, DeleteNonExistingPowerException, UserAlreadyVoteException {

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
}
