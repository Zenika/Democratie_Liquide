package com.zenika.liquid.democracy.api.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.model.Subject;

@Service
public class SubjectService {

	private static final Logger LOG = LoggerFactory.getLogger(SubjectService.class);

	@Autowired
	private SubjectRepository subjectRepository;

	public Subject addSubject(Subject s) throws MalformedSubjectException {
		LOG.info("Trying to add subject {}", s);

		if (!s.isWellFormed()) {
			throw new MalformedSubjectException();
		}

		s.setSubmitDate(new Date());

		LOG.info("Adding subject {}", s);
		return subjectRepository.save(s);
	}

	public List<Subject> getSubjectsInProgress() {
		LOG.info("Getting subjectsInProgress");

		List<Subject> out = subjectRepository.findByDeadLineGreaterThanOrDeadLineIsNull(new Date());
		return out;
	}

}
