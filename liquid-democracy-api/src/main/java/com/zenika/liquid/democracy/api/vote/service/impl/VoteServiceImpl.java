package com.zenika.liquid.democracy.api.vote.service.impl;

import com.zenika.liquid.democracy.api.subject.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.vote.exception.VoteForNonExistingSubjectException;
import com.zenika.liquid.democracy.api.vote.service.VoteService;
import com.zenika.liquid.democracy.api.vote.util.VoteUtil;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@EnableRetry
@Retryable(OptimisticLockingFailureException.class)
public class VoteServiceImpl implements VoteService {

    private final SubjectRepository subjectRepository;

    private final CollaboratorService collaboratorService;

    @Autowired
    public VoteServiceImpl(
            SubjectRepository subjectRepository,
            CollaboratorService collaboratorService

    ) {
        this.subjectRepository = subjectRepository;
        this.collaboratorService = collaboratorService;
    }

    @Override
    public void voteForSubject(String subjectUuid, Vote vote) {
        String userId = collaboratorService.currentUser().getEmail();

        Subject subject = subjectRepository.findSubjectByUuid(subjectUuid)
                .orElseThrow(VoteForNonExistingSubjectException::new);

        VoteUtil.checkVotes(vote, subject, userId);
        VoteUtil.prepareVotes(vote, subject, userId);

        subjectRepository.save(subject);
    }

}
