package com.zenika.liquid.democracy.api.subject.persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zenika.liquid.democracy.model.Subject;

public interface SubjectRepository extends MongoRepository<Subject, Long> {

	List<Subject> findByDeadLineGreaterThanOrDeadLineIsNull(Date d);

	Optional<Subject> findSubjectByUuid(String subjectUuid);

	List<Subject> findSubjectByCategoryUuid(String categoryUuid);
}
