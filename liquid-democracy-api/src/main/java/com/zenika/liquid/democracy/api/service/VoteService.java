package com.zenika.liquid.democracy.api.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.VoteForNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.VoteIsNotCorrectException;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.util.VoteUtil;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;

@Service
public class VoteService {

	private static final Logger LOG = LoggerFactory.getLogger(VoteService.class);

	@Autowired
	private SubjectRepository subjectRepository;

	public void voteForSubject(String subjectUuid, Vote vote, String userId) throws VoteForNonExistingSubjectException,
			VoteIsNotCorrectException, CloseSubjectException, UserAlreadyGavePowerException {

		LOG.info("Trying to vote for subject {} with {}", subjectUuid, vote);

		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);
		if (!s.isPresent()) {
			throw new VoteForNonExistingSubjectException();
		}

		LOG.info("Checking vote for subject {} with {}", subjectUuid, vote);
		VoteUtil.checkVotes(vote, s.get(), userId);

		LOG.info("Preparing vote for subject {} with {}", subjectUuid, vote);
		VoteUtil.prepareVotes(vote, s.get(), userId);

		LOG.info("Voting to vote for subject {} with {}", subjectUuid, vote);
		subjectRepository.save(s.get());
	}

}
