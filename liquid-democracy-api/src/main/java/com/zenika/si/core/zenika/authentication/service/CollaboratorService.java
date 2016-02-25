package com.zenika.si.core.zenika.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.si.core.zenika.authentication.persistence.CollaboratorRepository;

@Service
public class CollaboratorService {

	@Autowired
	CollaboratorRepository collaboratorRepository;

	private static final Logger LOG = LoggerFactory.getLogger(CollaboratorService.class);

}
