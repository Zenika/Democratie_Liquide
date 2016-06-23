package com.zenika.liquid.democracy.api.vote.service;

import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.vote.exception.VoteForNonExistingSubjectException;
import com.zenika.liquid.democracy.api.vote.exception.VoteIsNotCorrectException;
import com.zenika.liquid.democracy.model.Vote;

@Service
public interface VoteService {

	void voteForSubject(String subjectUuid, Vote vote) throws VoteForNonExistingSubjectException,
			VoteIsNotCorrectException, CloseSubjectException, UserAlreadyGavePowerException;

}