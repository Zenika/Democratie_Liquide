package com.zenika.liquid.democracy.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zenika.si.core.zenika.authentication.service.CollaboratorService;
import com.zenika.si.core.zenika.model.Collaborator;

@RestController
@RequestMapping("/api/collaborator")
public class CollaboratorController {
	private static final Logger LOG = LoggerFactory.getLogger(SubjectController.class);

	@Autowired
	private CollaboratorService collaboratorService;

	@RequestMapping(method = RequestMethod.GET, path = "/me")
	public ResponseEntity<Collaborator> getSubjectsInProgress() {

		LOG.info("getSubjectsInProgress");

		Collaborator out = collaboratorService.currentUser();

		return ResponseEntity.ok(out);
	}

}
