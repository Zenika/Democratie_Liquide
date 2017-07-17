package com.zenika.liquid.democracy.api.subject.service;

import com.zenika.liquid.democracy.dto.SubjectDto;
import com.zenika.liquid.democracy.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectService {

	SubjectDto addSubject(Subject s);

	void deleteSubject(String subjectUuid);

	List<SubjectDto> getSubjectsInProgress();

	List<SubjectDto> getSubjects();

	SubjectDto getSubjectByUuid(String subjectUuid);

}
