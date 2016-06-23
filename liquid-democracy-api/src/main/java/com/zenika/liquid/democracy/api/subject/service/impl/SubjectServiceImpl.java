package com.zenika.liquid.democracy.api.subject.service.impl;

import com.zenika.liquid.democracy.api.category.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.UnexistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.power.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.subject.exception.MalformedSubjectException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.subject.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.subject.service.SubjectService;
import com.zenika.liquid.democracy.api.power.util.PowerUtil;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.model.Category;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Subjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
		return subjectRepository.findByDeadLineGreaterThanOrDeadLineIsNull(new Date());
	}

	@Override
	public Subjects getSubjects() {
		final List<Subject> allSubjects = subjectRepository.findAll();
		final Map<Boolean, List<Subject>> subjectsByState = allSubjects.stream()
				.collect(
					Collectors.groupingBy(Subject::isClosed)
				);

		return new Subjects(subjectsByState.get(false), subjectsByState.get(true));
	}

	public Subject getSubjectByUuid(String subjectUuid) throws UnexistingSubjectException {
		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);

		if (!s.isPresent()) {
			throw new UnexistingSubjectException();
		}

		return s.get();
	}

}
