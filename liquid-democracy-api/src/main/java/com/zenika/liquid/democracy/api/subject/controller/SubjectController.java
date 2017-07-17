package com.zenika.liquid.democracy.api.subject.controller;

import com.zenika.liquid.democracy.api.exception.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.subject.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.subject.service.SubjectService;
import com.zenika.liquid.democracy.dto.SubjectDto;
import com.zenika.liquid.democracy.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addSubject(@Validated @RequestBody Subject s) {
        SubjectDto out = subjectService.addSubject(s);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(out.getUuid())
                        .toUri()
        ).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{subjectUuid}")
    public ResponseEntity<Void> deleteSubject(@PathVariable String subjectUuid) {
        subjectService.deleteSubject(subjectUuid);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<SubjectDto>> getSubjects() {
        return ResponseEntity.ok(subjectService.getSubjects());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/inprogress")
    public ResponseEntity<List<SubjectDto>> getSubjectsInProgress() {
        List<SubjectDto> out = subjectService.getSubjectsInProgress();
        if (out.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(out);
        }
        return ResponseEntity.ok(out);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{subjectUuid}")
    public ResponseEntity<SubjectDto> getSubjectByUuid(@PathVariable String subjectUuid) {
        SubjectDto s = subjectService.getSubjectByUuid(subjectUuid);
        return ResponseEntity.ok().body(s);
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
