package com.zenika.liquid.democracy.api.subject.controller;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.power.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.subject.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.subject.service.SubjectService;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Subjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> addSubject(@Validated @RequestBody Subject s)
	        throws MalformedSubjectException, AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, UserAlreadyVoteException, CloseSubjectException {

		Subject out = subjectService.addSubject(s);

		return ResponseEntity.created(
		        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(out.getUuid()).toUri())
		        .build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Subjects> getSubjects() {
		return ResponseEntity.ok(new SubjectsRepresentation(subjectService.getSubjects()));
	}

	@RequestMapping(method = RequestMethod.GET, path = "/inprogress")
	public ResponseEntity<List<SubjectRepresentation>> getSubjectsInProgress() throws MalformedSubjectException {

		List<SubjectRepresentation> out = subjectService.getSubjectsInProgress()
				.stream().map(SubjectRepresentation::new).collect(Collectors.toList());

		if (out.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(out);
		}

		return ResponseEntity.ok(out);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{subjectUuid}")
	public ResponseEntity<SubjectRepresentation> getSubjectByUuid(@PathVariable String subjectUuid)
	        throws UnexistingSubjectException {

		Subject s = subjectService.getSubjectByUuid(subjectUuid);

		return ResponseEntity.ok().body(new SubjectRepresentation(s));
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Le sujet n'existe pas")
	@ExceptionHandler(UnexistingSubjectException.class)
	public void unexistingSubjectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le sujet n'est pas complet")
	@ExceptionHandler(MalformedSubjectException.class)
	public void malFormedSubjectHandler() {
	}

}
