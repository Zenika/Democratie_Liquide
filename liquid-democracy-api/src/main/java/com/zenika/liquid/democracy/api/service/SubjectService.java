package com.zenika.liquid.democracy.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.exception.UnexistingSubjectException;
import com.zenika.liquid.democracy.model.Subject;

@Service
public interface SubjectService {

	public Subject addSubject(Subject s) throws MalformedSubjectException;

	public List<Subject> getSubjectsInProgress();

	public Subject getSubjectByUuid(String subjectUuid) throws UnexistingSubjectException;

}
