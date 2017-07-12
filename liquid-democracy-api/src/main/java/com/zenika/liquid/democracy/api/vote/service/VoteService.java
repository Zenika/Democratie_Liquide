package com.zenika.liquid.democracy.api.vote.service;

import com.zenika.liquid.democracy.model.Vote;
import org.springframework.stereotype.Service;

@Service
public interface VoteService {

	void voteForSubject(String subjectUuid, Vote vote);

}