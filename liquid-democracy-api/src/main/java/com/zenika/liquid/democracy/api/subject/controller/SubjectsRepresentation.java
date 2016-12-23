package com.zenika.liquid.democracy.api.subject.controller;

import com.zenika.liquid.democracy.model.Subjects;

import java.util.stream.Collectors;

/**
 * Subjects without the whole database
 */
public class SubjectsRepresentation extends Subjects {

    public SubjectsRepresentation(Subjects subjects) {
        this.setOpened(subjects.getOpened().stream().map(SubjectRepresentation::new).collect(Collectors.toList()));
        this.setClosed(subjects.getClosed().stream().map(SubjectRepresentation::new).collect(Collectors.toList()));
    }

}
