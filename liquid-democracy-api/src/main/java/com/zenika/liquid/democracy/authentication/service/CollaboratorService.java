package com.zenika.liquid.democracy.authentication.service;

import com.zenika.liquid.democracy.authentication.persistence.CollaboratorRepository;
import com.zenika.liquid.democracy.authentication.security.bean.SocialUserDetailsImpl;
import com.zenika.si.core.zenika.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaboratorService {

	private final CollaboratorRepository collaboratorRepository;

	@Autowired
	public CollaboratorService(CollaboratorRepository collaboratorRepository) {
		this.collaboratorRepository = collaboratorRepository;
	}

	private SocialUserDetailsImpl getAuthenticatedUser() {
		return (SocialUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public Collaborator currentUser() {
		return getAuthenticatedUser().getUser();
	}

	public List<Collaborator> getUsers() {
        return collaboratorRepository.findAll();
    }

}
