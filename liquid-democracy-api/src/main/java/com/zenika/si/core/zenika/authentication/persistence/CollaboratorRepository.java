package com.zenika.si.core.zenika.authentication.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zenika.si.core.zenika.model.Collaborator;

public interface CollaboratorRepository extends MongoRepository<Collaborator, Long> {

	Collaborator findCollaboratorByUsername(String collaboratorUsername);

	Collaborator findCollaboratorByEmail(String email);

}
