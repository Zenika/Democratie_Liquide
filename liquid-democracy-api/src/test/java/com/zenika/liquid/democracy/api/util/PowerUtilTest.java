package com.zenika.liquid.democracy.api.util;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import com.zenika.liquid.democracy.api.power.util.PowerUtil;
import org.junit.Test;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.power.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;
import com.zenika.liquid.democracy.model.WeightedChoice;

public class PowerUtilTest {

	@Test
	public void testCheckPowerForAddition_OK() throws UserAlreadyGavePowerException, UserGivePowerToHimselfException,
	        CloseSubjectException, UserAlreadyVoteException {
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

		Power p = new Power();
		p.setCollaboratorIdTo("julie.bourhis@zenika.com");

		PowerUtil.checkPowerForAddition(p, s, "sandra.parlant@zenika.com");

		assertTrue(true);
	}

	@Test(expected = CloseSubjectException.class)
	public void testCheckPowerForAddition_DeadLineKO() throws UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
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

		Power p = new Power();
		p.setCollaboratorIdTo("julie.bourhis@zenika.com");

		PowerUtil.checkPowerForAddition(p, s, "sandra.parlant@zenika.com");
	}

	@Test(expected = UserAlreadyGavePowerException.class)
	public void testCheckPowerForAddition_UserFromKO() throws UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException {
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

		Power p = new Power();
		p.setCollaboratorIdFrom("sandra.parlant@zenika.com");
		p.setCollaboratorIdTo("julie.bourhis@zenika.com");
		s.getPowers().add(p);

		Power pToAdd = new Power();
		pToAdd.setCollaboratorIdTo("guillaume.gerbaud@zenika.com");

		PowerUtil.checkPowerForAddition(p, s, "sandra.parlant@zenika.com");

		PowerUtil.checkPowerForAddition(p, s, "sandra.parlant@zenika.com");
	}

	@Test(expected = UserGivePowerToHimselfException.class)
	public void testCheckPowerForAddition_UserFromEqualsToKO() throws UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException {
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

		Power p = new Power();
		p.setCollaboratorIdTo("sandra.parlant@zenika.com");

		PowerUtil.checkPowerForAddition(p, s, "sandra.parlant@zenika.com");
	}

	@Test(expected = UserAlreadyVoteException.class)
	public void testCheckPowerForAddition_AlreadyVotedKO() throws UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, UserAlreadyVoteException, CloseSubjectException {
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

		Power p = new Power();
		p.setCollaboratorIdTo("julie.bourhis@zenika.com");

		Vote v = new Vote();
		v.setCollaboratorId("sandra.parlant@zenika.com");
		s.getVotes().add(v);

		PowerUtil.checkPowerForAddition(p, s, "sandra.parlant@zenika.com");
	}

	@Test
	public void testCheckPowerForAddition_CollabAlreadyVotedKO() throws UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, UserAlreadyVoteException, CloseSubjectException {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		Date deadLine = calendar.getTime();

		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		s.setDeadLine(deadLine);

		Proposition p1 = new Proposition();
		p1.setId("p001");
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");

		Power p = new Power();
		p.setCollaboratorIdTo("julie.bourhis@zenika.com");

		Vote v = new Vote();
		v.setCollaboratorId("julie.bourhis@zenika.com");
		WeightedChoice c = new WeightedChoice();
		c.setPoints(1);
		c.setPropositionId(p1.getId());
		v.getChoices().add(c);
		s.getVotes().add(v);

		PowerUtil.checkPowerForAddition(p, s, "sandra.parlant@zenika.com");
		PowerUtil.preparePower(p, s, "sandra.parlant@zenika.com", true);

		assertTrue(s.getPropositions().get(0).getPoints() == 2);
	}

	@Test
	public void testCheckPowerForDelete_OK()
	        throws DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException {
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

		Power p = new Power();
		p.setCollaboratorIdFrom("sandra.parlant@zenika.com");
		p.setCollaboratorIdTo("julie.bourhis@zenika.com");
		s.getPowers().add(p);

		PowerUtil.checkPowerForDelete(s, "sandra.parlant@zenika.com");

		assertTrue(true);
	}

	@Test(expected = CloseSubjectException.class)
	public void testCheckPowerForDelete_DeadLineKO()
	        throws DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
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

		Power p = new Power();
		p.setCollaboratorIdFrom("sandra.parlant@zenika.com");
		p.setCollaboratorIdTo("julie.bourhis@zenika.com");
		s.getPowers().add(p);

		PowerUtil.checkPowerForDelete(s, "sandra.parlant@zenika.com");
	}

	@Test(expected = DeleteNonExistingPowerException.class)
	public void testCheckPowerForDelete_UnexistingPowerKO()
	        throws DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException {
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

		PowerUtil.checkPowerForDelete(s, "sandra.parlant@zenika.com");
	}

	@Test(expected = UserAlreadyVoteException.class)
	public void testCheckPowerForDelete_AlreadyVotedKO()
	        throws DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException {
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

		Power p = new Power();
		p.setCollaboratorIdFrom("sandra.parlant@zenika.com");
		p.setCollaboratorIdTo("julie.bourhis@zenika.com");
		s.getPowers().add(p);

		Vote v = new Vote();
		v.setCollaboratorId("julie.bourhis@zenika.com");
		s.getVotes().add(v);

		PowerUtil.checkPowerForDelete(s, "sandra.parlant@zenika.com");
	}

}
