package com.zenika.liquid.democracy.api.util;

import java.util.Optional;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.UserGivePowerToHimselfException;
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
			return userId.equals(p.getCollaborateurIdFrom());
		}).findFirst();

		if (foundPower.isPresent()) {
			throw new UserAlreadyGavePowerException();
		}

		if (userId.equals(power.getCollaborateurIdTo())) {
			throw new UserGivePowerToHimselfException();
		}

		Optional<Vote> foundVote = subject.findVote(userId);
		if (foundVote.isPresent()) {
			throw new UserAlreadyVoteException();
		}

		foundVote = subject.findVote(power.getCollaborateurIdTo());
		if (foundVote.isPresent()) {
			throw new UserAlreadyVoteException();
		}

	}

	public static void preparePower(Power power, Subject s, String userId) {
		power.setCollaborateurIdFrom(userId);
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

		Optional<Vote> foundVote = subject.findVote(power.get().getCollaborateurIdTo());
		if (foundVote.isPresent()) {
			throw new UserAlreadyVoteException();
		}

		return power.get();

	}
}
