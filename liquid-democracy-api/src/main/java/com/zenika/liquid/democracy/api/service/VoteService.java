package com.zenika.liquid.democracy.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void voteForSubject(String subjectUuid, Vote vote)
			throws VoteForNonExistingSubjectException, VoteIsNotCorrectException {

		LOG.info("Trying to vote for subject {} with {}", subjectUuid, vote);

		Subject s = subjectRepository.findSubjectByUuid(subjectUuid);
		if (s == null) {
			throw new VoteForNonExistingSubjectException();
		}

		LOG.info("Checking vote for subject {} with {}", subjectUuid, vote);
		VoteUtil.checkVotes(vote, s);

		LOG.info("Preparing vote for subject {} with {}", subjectUuid, vote);
		VoteUtil.prepareVotes(vote, s);

		LOG.info("Voting to vote for subject {} with {}", subjectUuid, vote);
		subjectRepository.save(s);
	}

}
