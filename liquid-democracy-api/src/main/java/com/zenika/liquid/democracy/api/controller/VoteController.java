package com.zenika.liquid.democracy.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.zenika.liquid.democracy.api.exception.TooManyPointsException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.VoteForNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.VoteIsNotCorrectException;
import com.zenika.liquid.democracy.api.exception.VotePropositionIncorrectException;
import com.zenika.liquid.democracy.api.service.VoteService;
import com.zenika.liquid.democracy.api.util.AuthenticationUtil;
import com.zenika.liquid.democracy.model.Vote;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

	private static final Logger LOG = LoggerFactory.getLogger(VoteController.class);

	@Autowired
	private VoteService voteService;

	@RequestMapping(method = RequestMethod.PUT, value = "/{subjectUuid}")
	public ResponseEntity<Void> voteForSubject(OAuth2Authentication authentication, @PathVariable String subjectUuid,
			@Validated @RequestBody Vote vote) throws VoteForNonExistingSubjectException, VoteIsNotCorrectException {

		LOG.info("voteForSubject {} with {} ", subjectUuid, vote);

		String userId = AuthenticationUtil.getUserIdentifiant(authentication);

		voteService.voteForSubject(subjectUuid, vote, userId);

		return ResponseEntity.ok().build();
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Subject doesn't exist")
	@ExceptionHandler(VoteForNonExistingSubjectException.class)
	public void voteForNonExistingSubjectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Propositions voted are not correct")
	@ExceptionHandler(VotePropositionIncorrectException.class)
	public void votePropositionIncorrectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Too many points used")
	@ExceptionHandler(TooManyPointsException.class)
	public void tooManyPointsHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User has already voted")
	@ExceptionHandler(UserAlreadyVoteException.class)
	public void userAlreadyVoteHandler() {
	}

}
