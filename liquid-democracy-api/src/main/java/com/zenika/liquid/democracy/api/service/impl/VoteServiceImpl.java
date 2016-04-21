package com.zenika.liquid.democracy.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.vote.VoteForNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.vote.VoteIsNotCorrectException;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.service.VoteService;
import com.zenika.liquid.democracy.api.util.VoteUtil;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;

@Service
@EnableRetry
@Retryable(OptimisticLockingFailureException.class)
public class VoteServiceImpl implements VoteService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private CollaboratorService collaboratorService;

	@Override
	public void voteForSubject(String subjectUuid, Vote vote) throws VoteForNonExistingSubjectException,
			VoteIsNotCorrectException, CloseSubjectException, UserAlreadyGavePowerException {

		String userId = collaboratorService.currentUser().getEmail();

		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);
		if (!s.isPresent()) {
			throw new VoteForNonExistingSubjectException();
		}

		VoteUtil.checkVotes(vote, s.get(), userId);

		VoteUtil.prepareVotes(vote, s.get(), userId);

		subjectRepository.save(s.get());
	}

}
