package com.zenika.liquid.democracy.api.subject.service.impl;

import com.zenika.liquid.democracy.api.category.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.channel.persistence.ChannelRepository;
import com.zenika.liquid.democracy.api.exception.UndeletableSubjectException;
import com.zenika.liquid.democracy.api.exception.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.power.util.PowerUtil;
import com.zenika.liquid.democracy.api.subject.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.subject.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.subject.service.SubjectService;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.config.MapperConfig;
import com.zenika.liquid.democracy.dto.PropositionDto;
import com.zenika.liquid.democracy.dto.SubjectDto;
import com.zenika.liquid.democracy.model.Category;
import com.zenika.liquid.democracy.model.Channel;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final CategoryRepository categoryRepository;

    private final ChannelRepository channelRepository;

    private final CollaboratorService collaboratorService;

    private final MapperConfig mapper;

    @Autowired
    public SubjectServiceImpl(
            SubjectRepository subjectRepository,
            CategoryRepository categoryRepository,
            ChannelRepository channelRepository,
            CollaboratorService collaboratorService,
            MapperConfig mapper
    ) {
        this.subjectRepository = subjectRepository;
        this.categoryRepository = categoryRepository;
        this.channelRepository = channelRepository;
        this.collaboratorService = collaboratorService;
        this.mapper = mapper;
    }

    public SubjectDto addSubject(Subject subject) {
        String userId = collaboratorService.currentUser().getEmail();

        if (subject.getCategory() != null) {
            Category category = categoryRepository.findCategoryByUuid(subject.getCategory().getUuid())
                    .orElseThrow(MalformedSubjectException::new);
            subject.setCategory(category);
        }

        if (subject.getChannel() != null) {
            Channel channel = channelRepository.findChannelByUuid(subject.getChannel().getUuid())
                    .orElseThrow(MalformedSubjectException::new);
            subject.setChannel(channel);
        }

        if (!subject.isWellFormed()) {
            throw new MalformedSubjectException();
        }

        subject.setSubmitDate(new Date());
        subject.setCollaboratorId(userId);

        subject = subjectRepository.save(subject);

        if (subject.getCategory() != null) {
            for (Power power : subject.getCategory().getPowers()) {
                Power powerTmp = new Power();
                powerTmp.setCollaboratorIdTo(power.getCollaboratorIdTo());

                PowerUtil.checkPowerForAddition(powerTmp, subject, power.getCollaboratorIdFrom());
                PowerUtil.preparePower(powerTmp, subject, power.getCollaboratorIdFrom());
            }
        }

        return prepareSubjectForResponse(subjectRepository.save(subject), userId);
    }

    public void deleteSubject(String subjectUuid) {
        Subject subject = subjectRepository.findSubjectByUuid(subjectUuid)
                .orElseThrow(UnexistingSubjectException::new);

        String userId = collaboratorService.currentUser().getEmail();

        if (!subject.isMine(userId) || subject.getVoteCount() != 0) {
            throw new UndeletableSubjectException();
        }

        subjectRepository.delete(subject);
    }

    public List<SubjectDto> getSubjectsInProgress() {
        String userId = collaboratorService.currentUser().getEmail();
        return subjectRepository
                .findByDeadLineGreaterThanOrDeadLineIsNull(new Date())
                .stream()
                .map(s -> prepareSubjectForResponse(s, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> getSubjects() {
        String userId = collaboratorService.currentUser().getEmail();
        return subjectRepository
                .findAll()
                .stream()
                .map(s -> prepareSubjectForResponse(s, userId))
                .collect(Collectors.toList());
    }

    public SubjectDto getSubjectByUuid(String subjectUuid) {
        Subject subject = subjectRepository.findSubjectByUuid(subjectUuid)
                .orElseThrow(UnexistingSubjectException::new);

        String userId = collaboratorService.currentUser().getEmail();

        return prepareSubjectForResponse(subject, userId);
    }

    private SubjectDto prepareSubjectForResponse(Subject s, String userId) {
        SubjectDto sdto = mapper.map(s, SubjectDto.class);
        sdto.setIsClosed(s.isClosed());
        sdto.setIsMine(s.isMine(userId));
        sdto.setIsVoted(s.isVoted(userId));
        sdto.setGivenDelegation(s.getGivenDelegation(userId));
        sdto.setReceivedDelegations(s.getReceivedDelegations(userId));
        sdto.setVoteCount(s.getVoteCount());
        List<PropositionDto> propositions = sdto.getPropositions();
        if (!sdto.getIsVoted() && !sdto.getIsClosed()) {
            sdto.setPropositions(propositions.stream().map(p -> {
                p.setPoints(0);
                return p;
            }).collect(Collectors.toList()));
        }
        return sdto;
    }

}
