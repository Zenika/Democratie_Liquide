package com.zenika.liquid.democracy.api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.commons.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.power.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.exception.subject.MalformedSubjectException;
import com.zenika.liquid.democracy.api.exception.vote.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.service.SubjectService;
import com.zenika.liquid.democracy.api.util.PowerUtil;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.model.Category;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;

@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CollaboratorService collaboratorService;

	public Subject addSubject(Subject s)
	        throws MalformedSubjectException, AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, UserAlreadyVoteException, CloseSubjectException {

		String userId = collaboratorService.currentUser().getEmail();

		if (s.getCategory() != null) {
			Optional<Category> category = categoryRepository.findCategoryByUuid(s.getCategory().getUuid());

			if (!category.isPresent()) {
				throw new MalformedSubjectException();
			} else {
				s.setCategory(category.get());
			}
		}

		if (!s.isWellFormed()) {
			throw new MalformedSubjectException();
		}

		s.setSubmitDate(new Date());
		s.setCollaboratorId(userId);

		s = subjectRepository.save(s);

		if (s.getCategory() != null) {
			for (Power power : s.getCategory().getPowers()) {
				Power powerTmp = new Power();
				powerTmp.setCollaboratorIdTo(power.getCollaboratorIdTo());

				boolean addVote = PowerUtil.checkPowerForAddition(powerTmp, s, power.getCollaboratorIdFrom());

				PowerUtil.preparePower(powerTmp, s, power.getCollaboratorIdFrom(), addVote);
			}
		}

		return subjectRepository.save(s);
	}

	public List<Subject> getSubjectsInProgress() {
		List<Subject> out = subjectRepository.findByDeadLineGreaterThanOrDeadLineIsNull(new Date());
		return out;
	}

	public Subject getSubjectByUuid(String subjectUuid) throws UnexistingSubjectException {
		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);

		if (!s.isPresent()) {
			throw new UnexistingSubjectException();
		}

		return s.get();
	}

}
