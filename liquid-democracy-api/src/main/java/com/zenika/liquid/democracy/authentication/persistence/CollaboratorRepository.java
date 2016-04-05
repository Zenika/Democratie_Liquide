package com.zenika.liquid.democracy.authentication.persistence;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zenika.si.core.zenika.model.Collaborator;

public interface CollaboratorRepository extends MongoRepository<Collaborator, Long> {

	Optional<Collaborator> findCollaboratorByEmail(String email);

}
