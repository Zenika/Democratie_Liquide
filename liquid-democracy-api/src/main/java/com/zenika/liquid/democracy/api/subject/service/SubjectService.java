package com.zenika.liquid.democracy.api.subject.service;

import java.util.List;

import com.zenika.liquid.democracy.dto.SubjectDto;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.power.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.subject.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.model.Subject;

@Service
public interface SubjectService {

	public Subject addSubject(Subject s)
	        throws MalformedSubjectException, AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, UserAlreadyVoteException, CloseSubjectException;

	public List<Subject> getSubjectsInProgress();

	public List<SubjectDto> getSubjects();

	public Subject getSubjectByUuid(String subjectUuid) throws UnexistingSubjectException;

}
