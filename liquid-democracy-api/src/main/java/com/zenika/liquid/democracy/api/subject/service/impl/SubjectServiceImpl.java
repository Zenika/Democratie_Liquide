package com.zenika.liquid.democracy.api.subject.service.impl;

import com.zenika.liquid.democracy.api.category.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.channel.persistence.ChannelRepository;
import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.UndeletableSubjectException;
import com.zenika.liquid.democracy.api.exception.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.power.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.power.util.PowerUtil;
import com.zenika.liquid.democracy.api.subject.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.subject.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.subject.service.SubjectService;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.config.MapperConfig;
import com.zenika.liquid.democracy.dto.SubjectDto;
import com.zenika.liquid.democracy.model.Category;
import com.zenika.liquid.democracy.model.Channel;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private CollaboratorService collaboratorService;


    @Autowired
    MapperConfig mapper;

    public SubjectDto addSubject(Subject s)
            throws MalformedSubjectException, AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
            UserGivePowerToHimselfException, UserAlreadyVoteException, CloseSubjectException {

        String userId = collaboratorService.currentUser().getEmail();

        if (s.getCategory() != null) {
            Optional<Category> category = categoryRepository.findCategoryByUuid(s.getCategory().getUuid());

            if (!category.isPresent()) {
                throw new MalformedSubjectException();
            } else {
                s.setCategory(category.get());
            }
        }

        if (s.getChannel() != null) {
            Optional<Channel> channel = channelRepository.findChannelByUuid(s.getChannel().getUuid());

            if (!channel.isPresent()) {
                throw new MalformedSubjectException();
            } else {
                s.setChannel(channel.get());
            }
        }

        if (!s.isWellFormed()) {
            throw new MalformedSubjectException();
        }

        s.setSubmitDate(new Date());
        s.setCollaboratorId(userId);

        s = subjectRepository.save(s);

        if (s.getCategory() != null) {
            for (Power power : s.getCategory().getPowers()) {
                Power powerTmp = new Power();
                powerTmp.setCollaboratorIdTo(power.getCollaboratorIdTo());

                boolean addVote = PowerUtil.checkPowerForAddition(powerTmp, s, power.getCollaboratorIdFrom());

                PowerUtil.preparePower(powerTmp, s, power.getCollaboratorIdFrom(), addVote);
            }
        }

        return prepareSubjectForResponse(subjectRepository.save(s), userId);
    }

    public void deleteSubject(String subjectUuid) throws UnexistingSubjectException, UndeletableSubjectException {
        Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);
        String userId = collaboratorService.currentUser().getEmail();

        if (!s.isPresent()) {
            throw new UnexistingSubjectException();
        }

        Subject sub = s.get();
        if (!sub.isMine(userId) || sub.getVoteCount() != 0) {
            throw new UndeletableSubjectException();
        } else {
            subjectRepository.delete(sub);
        }
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

    public SubjectDto getSubjectByUuid(String subjectUuid) throws UnexistingSubjectException {
        Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);
        String userId = collaboratorService.currentUser().getEmail();

        if (!s.isPresent()) {
            throw new UnexistingSubjectException();
        }

        return prepareSubjectForResponse(s.get(), userId);
    }

    private SubjectDto prepareSubjectForResponse(Subject s, String userId) {
        SubjectDto sdto = mapper.map(s, SubjectDto.class);
        sdto.setIsClosed(s.isClosed());
        sdto.setIsMine(s.isMine(userId));
        sdto.setIsVoted(s.isVoted(userId));
        sdto.setGivenDelegation(s.getGivenDelegation(userId));
        sdto.setReceivedDelegations(s.getReceivedDelegations(userId));
        sdto.setVoteCount(s.getVoteCount());
        return sdto;
    }

}
