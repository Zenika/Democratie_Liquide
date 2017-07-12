package com.zenika.liquid.democracy.api.vote.controller;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.vote.exception.TooManyPointsException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.vote.exception.VoteForNonExistingSubjectException;
import com.zenika.liquid.democracy.api.vote.exception.VotePropositionIncorrectException;
import com.zenika.liquid.democracy.api.vote.service.VoteService;
import com.zenika.liquid.democracy.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

	private final VoteService voteService;

	@Autowired
	public VoteController(VoteService voteService) {
	    this.voteService = voteService;
    }

	@RequestMapping(method = RequestMethod.PUT, value = "/{subjectUuid}")
	public ResponseEntity<Void> voteForSubject(@PathVariable String subjectUuid, @Validated @RequestBody Vote vote) {
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
