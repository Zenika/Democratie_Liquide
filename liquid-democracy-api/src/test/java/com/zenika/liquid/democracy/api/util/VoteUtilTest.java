package com.zenika.liquid.democracy.api.util;

import static org.junit.Assert.assertEquals;

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

	@Test
	public void testCheckVote_OK() throws VotePropositionIncorrectException, TooManyPointsException,
			UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Subject s = generateSubject();

		Vote v = new Vote();
		WeightedChoice choice1 = new WeightedChoice();
		choice1.setProposition(s.getPropositions().get(0));
		choice1.setPoints(1);
		v.getChoices().add(choice1);

		VoteUtil.checkVotes(v, s, "sandra.parlant@zenika.com");
	}

	@Test
	public void compileVoteTest() throws VotePropositionIncorrectException, TooManyPointsException,
			UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Subject s = generateSubject();

		Vote v0 = new Vote();
		v0.setCollaborateurId("julie.bourhis@zenika.com");
		WeightedChoice choice01 = new WeightedChoice();
		choice01.setProposition(s.getPropositions().get(1));
		choice01.setPoints(1);
		v0.getChoices().add(choice01);
		s.getVotes().add(v0);

		Vote v1 = new Vote();
		v1.setCollaborateurId("julie.bourhis@zenika.com");
		WeightedChoice choice11 = new WeightedChoice();
		choice11.setProposition(s.getPropositions().get(1));
		choice11.setPoints(1);
		v1.getChoices().add(choice11);
		s.getVotes().add(v1);

		Vote v = new Vote();
		WeightedChoice choice1 = new WeightedChoice();
		choice1.setProposition(s.getPropositions().get(0));
		choice1.setPoints(1);
		v.getChoices().add(choice1);

		VoteUtil.prepareVotes(v, s, "sandra.parlant@zenika.com");

		assertEquals(1, s.getPropositions().get(0).getPoints());
		assertEquals(2, s.getPropositions().get(1).getPoints());

		Vote v3 = new Vote();
		WeightedChoice choice3 = new WeightedChoice();
		choice3.setProposition(s.getPropositions().get(1));
		choice3.setPoints(1);
		v.getChoices().add(choice3);
		VoteUtil.prepareVotes(v3, s, "sandra.parlant@zenika.com");

		assertEquals(1, s.getPropositions().get(0).getPoints());
		assertEquals(3, s.getPropositions().get(1).getPoints());
	}

	@Test
	public void prepareVoteWithPowerTest() throws VotePropositionIncorrectException, TooManyPointsException,
			UserAlreadyVoteException, UserAlreadyGavePowerException, CloseSubjectException {
		Subject s = generateSubject();

		Power p = new Power();
		p.setCollaborateurIdFrom("sandra.parlant@zenika.com");
		p.setCollaborateurIdTo("julie.bourhis@zenika.com");
		s.getPowers().add(p);

		Vote v0 = new Vote();
		v0.setCollaborateurId("julie.bourhis@zenika.com");
		WeightedChoice choice01 = new WeightedChoice();
		choice01.setProposition(s.getPropositions().get(1));
		choice01.setPoints(1);
		v0.getChoices().add(choice01);
		s.getVotes().add(v0);

		VoteUtil.prepareVotes(v0, s, "sandra.parlant@zenika.com");

		assertEquals(0, s.getPropositions().get(0).getPoints());
		assertEquals(2, s.getPropositions().get(1).getPoints());
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
