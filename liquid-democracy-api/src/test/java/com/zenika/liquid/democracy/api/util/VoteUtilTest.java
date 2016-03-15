package com.zenika.liquid.democracy.api.util;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.TooManyPointsException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.VotePropositionIncorrectException;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;
import com.zenika.liquid.democracy.model.WeightedChoice;

public class VoteUtilTest {

	@Test(expected = VotePropositionIncorrectException.class)
	public void testCheckVote_VoteForSamePropositionKO() throws VotePropositionIncorrectException,
			TooManyPointsException, UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Subject s = generateSubject();
		s.setMaxPoints(2);

		Vote v = new Vote();
		WeightedChoice choice2 = new WeightedChoice();
		choice2.setProposition(s.getPropositions().get(1));
		choice2.setPoints(1);
		v.getChoices().add(choice2);

		WeightedChoice choice1 = new WeightedChoice();
		choice1.setProposition(s.getPropositions().get(1));
		choice1.setPoints(1);
		v.getChoices().add(choice1);

		VoteUtil.checkVotes(v, s, "sandra.parlant@zenika.com");

	}

	@Test(expected = VotePropositionIncorrectException.class)
	public void testCheckVote_PropositionNotFoundKO() throws VotePropositionIncorrectException, TooManyPointsException,
			UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Subject s = generateSubject();

		Vote v = new Vote();
		WeightedChoice choice1 = new WeightedChoice();
		Proposition p2 = new Proposition();
		p2.setTitle("P2 title");
		choice1.setProposition(p2);
		choice1.setPoints(1);
		v.getChoices().add(choice1);

		VoteUtil.checkVotes(v, s, "sandra.parlant@zenika.com");

	}

	@Test(expected = TooManyPointsException.class)
	public void testCheckVote_TooManyPointsKO() throws VotePropositionIncorrectException, TooManyPointsException,
			UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Subject s = generateSubject();

		Vote v = new Vote();
		WeightedChoice choice2 = new WeightedChoice();
		choice2.setProposition(s.getPropositions().get(1));
		choice2.setPoints(1);
		v.getChoices().add(choice2);

		WeightedChoice choice1 = new WeightedChoice();
		choice1.setProposition(s.getPropositions().get(0));
		choice1.setPoints(1);
		v.getChoices().add(choice1);

		VoteUtil.checkVotes(v, s, "sandra.parlant@zenika.com");

	}

	@Test(expected = UserAlreadyGavePowerException.class)
	public void testCheckVote_GavePowerKO() throws VotePropositionIncorrectException, TooManyPointsException,
			UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Subject s = generateSubject();

		Power p = new Power();
		p.setCollaborateurIdFrom("sandra.parlant@zenika.com");
		s.getPowers().add(p);

		Vote v = new Vote();
		WeightedChoice choice1 = new WeightedChoice();
		choice1.setProposition(s.getPropositions().get(0));
		choice1.setPoints(1);
		v.getChoices().add(choice1);

		VoteUtil.checkVotes(v, s, "sandra.parlant@zenika.com");

	}

	@Test(expected = UserAlreadyVoteException.class)
	public void testCheckVote_AlreadyVotedKO() throws VotePropositionIncorrectException, TooManyPointsException,
			UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Subject s = generateSubject();

		Vote oldV = new Vote();
		oldV.setCollaborateurId("sandra.parlant@zenika.com");
		WeightedChoice oldChoice1 = new WeightedChoice();
		oldChoice1.setProposition(s.getPropositions().get(0));
		oldChoice1.setPoints(1);
		oldV.getChoices().add(oldChoice1);

		s.getVotes().add(oldV);

		Vote v = new Vote();
		WeightedChoice choice1 = new WeightedChoice();
		choice1.setProposition(s.getPropositions().get(0));
		choice1.setPoints(1);
		v.getChoices().add(choice1);

		VoteUtil.checkVotes(v, s, "sandra.parlant@zenika.com");

	}

	@Test(expected = CloseSubjectException.class)
	public void testCheckVote_DeadLineKO() throws VotePropositionIncorrectException, TooManyPointsException,
			UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date deadLine = calendar.getTime();

		Subject s = generateSubject();
		s.setDeadLine(deadLine);

		Vote v = new Vote();
		WeightedChoice choice1 = new WeightedChoice();
		choice1.setProposition(s.getPropositions().get(0));
		choice1.setPoints(1);
		v.getChoices().add(choice1);

		VoteUtil.checkVotes(v, s, "sandra.parlant@zenika.com");

	}

	private Subject generateSubject() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		Date deadLine = calendar.getTime();

		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		s.setDeadLine(deadLine);

		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");

		return s;
	}

}
