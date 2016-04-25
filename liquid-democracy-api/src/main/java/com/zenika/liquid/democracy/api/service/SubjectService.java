package com.zenika.liquid.democracy.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.commons.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.power.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.exception.subject.MalformedSubjectException;
import com.zenika.liquid.democracy.api.exception.vote.UserAlreadyVoteException;
import com.zenika.liquid.democracy.model.Subject;

@Service
public interface SubjectService {

	public Subject addSubject(Subject s)
	        throws MalformedSubjectException, AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, UserAlreadyVoteException, CloseSubjectException;

	public List<Subject> getSubjectsInProgress();

	public Subject getSubjectByUuid(String subjectUuid) throws UnexistingSubjectException;

}
