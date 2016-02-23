package com.zenika.liquid.democracy.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;

import liquid.democracy.model.Subject;

@Service
public class SubjectService {

	private static final Logger LOG = LoggerFactory.getLogger(SubjectService.class);

	@Autowired
	private SubjectRepository subjectRepository;

	public Subject addSubject(Subject s) throws MalformedSubjectException {
		if (!checkIfSubjectIsWellFormed(s)) {
			throw new MalformedSubjectException();
		}
		return subjectRepository.save(s);
	}

	private boolean checkIfSubjectIsWellFormed(Subject s) {
		return s.isWellFormed();
	}

}
