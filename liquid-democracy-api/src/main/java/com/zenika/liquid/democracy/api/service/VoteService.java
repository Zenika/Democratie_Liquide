package com.zenika.liquid.democracy.api.service;

import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.vote.VoteForNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.vote.VoteIsNotCorrectException;
import com.zenika.liquid.democracy.model.Vote;

@Service
public interface VoteService {

	void voteForSubject(String subjectUuid, Vote vote) throws VoteForNonExistingSubjectException,
			VoteIsNotCorrectException, CloseSubjectException, UserAlreadyGavePowerException;

}