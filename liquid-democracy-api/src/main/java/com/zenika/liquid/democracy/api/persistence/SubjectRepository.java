package com.zenika.liquid.democracy.api.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import liquid.democracy.model.Subject;

public interface SubjectRepository extends MongoRepository<Subject, Long> {

}
