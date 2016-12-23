package com.zenika.liquid.democracy.api.subject.controller;

import com.zenika.liquid.democracy.model.Subject;

/**
 * Subject without the whole database
 */
public class SubjectRepresentation extends Subject {


    public SubjectRepresentation(Subject s) {
        this.setId(s.getUuid());
        this.setTitle(s.getTitle());
        this.setDescription(s.getDescription());
        this.setDeadLine(s.getDeadLine());
        this.setVersion(s.getVersion());
        this.setMaxPoints(s.getMaxPoints());
        this.setSubmitDate(s.getSubmitDate());
        this.setCollaboratorId(s.getCollaboratorId());
        this.setPropositions(s.getPropositions());
        this.setChannel(s.getChannel());
        this.setCategory(s.getCategory());
    }

}
