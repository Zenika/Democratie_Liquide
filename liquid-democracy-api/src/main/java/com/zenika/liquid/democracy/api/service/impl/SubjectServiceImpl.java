package com.zenika.liquid.democracy.api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.commons.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.exception.subject.MalformedSubjectException;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.service.SubjectService;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.model.Subject;

@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private CollaboratorService collaboratorService;

	public Subject addSubject(Subject s) throws MalformedSubjectException {

		String userId = collaboratorService.currentUser().getEmail();

		if (!s.isWellFormed()) {
			throw new MalformedSubjectException();
		}

		s.setSubmitDate(new Date());

		s.setCollaboratorId(userId);

		return subjectRepository.save(s);
	}

	public List<Subject> getSubjectsInProgress() {
		List<Subject> out = subjectRepository.findByDeadLineGreaterThanOrDeadLineIsNull(new Date());
		return out;
	}

	public Subject getSubjectByUuid(String subjectUuid) throws UnexistingSubjectException {
		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);

		if (!s.isPresent()) {
			throw new UnexistingSubjectException();
		}

		return s.get();
	}

}
