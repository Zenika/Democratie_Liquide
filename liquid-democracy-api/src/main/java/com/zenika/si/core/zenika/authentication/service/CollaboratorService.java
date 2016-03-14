package com.zenika.si.core.zenika.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zenika.si.core.zenika.authentication.persistence.CollaboratorRepository;
import com.zenika.si.core.zenika.authentication.security.bean.SocialUserDetailsImpl;
import com.zenika.si.core.zenika.model.Collaborator;

@Service
public class CollaboratorService {

	@Autowired
	CollaboratorRepository collaboratorRepository;

	private static final Logger LOG = LoggerFactory.getLogger(CollaboratorService.class);

	private SocialUserDetailsImpl getAuthenticatedUser() {
		return (SocialUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public Collaborator currentUser() {
		return getAuthenticatedUser().getUser();
	}

}
