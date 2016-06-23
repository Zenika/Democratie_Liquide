package com.zenika.liquid.democracy.api.collaborator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.si.core.zenika.model.Collaborator;

@RestController
@RequestMapping("/api/collaborator")
public class CollaboratorController {
	@Autowired
	private CollaboratorService collaboratorService;

	@RequestMapping(method = RequestMethod.GET, path = "/me")
	public ResponseEntity<Collaborator> getCurrentUser() {

		Collaborator out = collaboratorService.currentUser();

		return ResponseEntity.ok(out);
	}

}
