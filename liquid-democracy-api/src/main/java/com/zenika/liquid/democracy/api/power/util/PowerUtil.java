package com.zenika.liquid.democracy.api.power.util;

import java.util.Optional;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.power.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.vote.util.VoteUtil;
import com.zenika.liquid.democracy.model.Category;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;

public class PowerUtil {

	public static boolean checkPowerForAddition(Power power, Subject subject, String userId)
	        throws UserAlreadyGavePowerException, UserGivePowerToHimselfException, CloseSubjectException,
	        UserAlreadyVoteException {

		boolean addVote = false;

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
			addVote = true;
		}

		return addVote;

	}

	public static void checkCategoryPowerForAddition(Power power, Category category, String userId)
	        throws UserAlreadyGavePowerException, UserGivePowerToHimselfException {
		Optional<Power> foundPower = category.getPowers().stream().filter(p -> {
			return userId.equals(p.getCollaboratorIdFrom());
		}).findFirst();

		if (foundPower.isPresent()) {
			throw new UserAlreadyGavePowerException();
		}

		foundPower = category.getPowers().stream().filter(p -> {
			return power.getCollaboratorIdTo().equals(p.getCollaboratorIdFrom());
		}).findFirst();

		if (foundPower.isPresent()) {
			throw new UserAlreadyGavePowerException();
		}

		if (userId.equals(power.getCollaboratorIdTo())) {
			throw new UserGivePowerToHimselfException();
		}

	}

	public static void preparePower(Power power, Subject s, String userId, boolean addVote) {
		power.setCollaboratorIdFrom(userId);

		if (addVote) {
			Optional<Vote> voteToDuplicate = s.findVote(power.getCollaboratorIdTo());
			Vote newVote = new Vote();
			newVote.setCollaboratorId(userId);
			newVote.setChoices(voteToDuplicate.get().getChoices());
			s.getVotes().add(newVote);
		}

		s.getPowers().add(power);

		VoteUtil.compileResults(s);
	}

	public static void prepareCategoryPower(Power power, Category c, String userId) {
		power.setCollaboratorIdFrom(userId);
		c.getPowers().add(power);
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

	public static Power checkCategoryPowerForDelete(Category category, String userId)
	        throws DeleteNonExistingPowerException {

		Optional<Power> power = category.findPower(userId);
		if (!power.isPresent()) {
			throw new DeleteNonExistingPowerException();
		}

		return power.get();

	}
}
