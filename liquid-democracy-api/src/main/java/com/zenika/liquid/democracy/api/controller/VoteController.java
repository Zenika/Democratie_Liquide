package com.zenika.liquid.democracy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.TooManyPointsException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.VoteForNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.VoteIsNotCorrectException;
import com.zenika.liquid.democracy.api.exception.VotePropositionIncorrectException;
import com.zenika.liquid.democracy.api.service.VoteService;
import com.zenika.liquid.democracy.model.Vote;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

	@Autowired
	private VoteService voteService;

	@RequestMapping(method = RequestMethod.PUT, value = "/{subjectUuid}")
	public ResponseEntity<Void> voteForSubject(@PathVariable String subjectUuid, @Validated @RequestBody Vote vote)
			throws VoteForNonExistingSubjectException, VoteIsNotCorrectException, CloseSubjectException,
			UserAlreadyGavePowerException {

		voteService.voteForSubject(subjectUuid, vote);

		return ResponseEntity.ok().build();
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le sujet est terminé")
	@ExceptionHandler(CloseSubjectException.class)
	public void voteForClosedSubjectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le sujet n'existe pas")
	@ExceptionHandler(VoteForNonExistingSubjectException.class)
	public void voteForNonExistingSubjectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Les propositions votées ne sont pas correctes")
	@ExceptionHandler(VotePropositionIncorrectException.class)
	public void votePropositionIncorrectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous avez utilisé trop de points")
	@ExceptionHandler(TooManyPointsException.class)
	public void tooManyPointsHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous avez déjà voté")
	@ExceptionHandler(UserAlreadyVoteException.class)
	public void userAlreadyVoteHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous avez donné une délégation pour ce sujet")
	@ExceptionHandler(UserAlreadyGavePowerException.class)
	public void userAlreadyGavePowerHandler() {
	}

}
