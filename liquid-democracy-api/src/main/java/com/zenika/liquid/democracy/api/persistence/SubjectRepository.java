package com.zenika.liquid.democracy.api.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import liquid.democracy.model.Subject;

public interface SubjectRepository extends MongoRepository<Subject, Long> {

	List<Subject> findByDeadLineGreaterThanOrDeadLineIsNull(Date d);

}
