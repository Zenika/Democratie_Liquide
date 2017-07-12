package com.zenika.liquid.democracy.api.power.service.impl;

import com.zenika.liquid.democracy.api.category.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.*;
import com.zenika.liquid.democracy.api.power.service.PowerService;
import com.zenika.liquid.democracy.api.power.util.PowerUtil;
import com.zenika.liquid.democracy.api.subject.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.model.Category;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EnableRetry
@Retryable(OptimisticLockingFailureException.class)
public class PowerServiceImpl implements PowerService {

	private final SubjectRepository subjectRepository;

	private final CategoryRepository categoryRepository;

	private final CollaboratorService collaboratorService;

	@Autowired
	public PowerServiceImpl(SubjectRepository subjectRepository, CategoryRepository categoryRepository, CollaboratorService collaboratorService) {
	    this.subjectRepository = subjectRepository;
	    this.categoryRepository = categoryRepository;
	    this.collaboratorService = collaboratorService;
    }

	public void addPowerOnSubject(final String subjectUuid, final Power power) {
		final String userId = collaboratorService.currentUser().getEmail();

		final Subject subject = subjectRepository.findSubjectByUuid(subjectUuid)
                .orElseThrow(AddPowerOnNonExistingSubjectException::new);

		PowerUtil.checkPowerForAddition(power, subject, userId);
		PowerUtil.preparePower(power, subject, userId);

		subjectRepository.save(subject);
	}

	@Override
	public void addPowerOnCategory(final String categoryUuid, final Power power) {
		final String userId = collaboratorService.currentUser().getEmail();

		final Category category = categoryRepository.findCategoryByUuid(categoryUuid)
                .orElseThrow(AddPowerOnNonExistingCategoryException::new);

		PowerUtil.checkCategoryPowerForAddition(power, category, userId);
		PowerUtil.prepareCategoryPower(power, category, userId);

		final List<Subject> subjectsInCategory = subjectRepository.findSubjectByCategoryUuid(categoryUuid);
		for (Subject subject : subjectsInCategory) {
			try {
				Power powerTmp = new Power();
				powerTmp.setCollaboratorIdTo(power.getCollaboratorIdTo());
				addPowerOnSubject(subject.getUuid(), powerTmp);
			} catch (PowerIsNotCorrectException | UserAlreadyVoteException | CloseSubjectException e) {
				// if this appends, we just don't propagate power
				System.out.println("ERROR DELEGATION");
			}
		}

		categoryRepository.save(category);
	}

	public void deletePowerOnSubject(final String subjectUuid) {
		final String userId = collaboratorService.currentUser().getEmail();

		final Subject s = subjectRepository.findSubjectByUuid(subjectUuid)
                .orElseThrow(DeletePowerOnNonExistingSubjectException::new);

		final Power power = PowerUtil.checkPowerForDelete(s, userId);

		s.removePower(power);

		subjectRepository.save(s);
	}

	@Override
	public void deletePowerOnCategory(String categoryUuid) {
		final String userId = collaboratorService.currentUser().getEmail();

		final Category c = categoryRepository.findCategoryByUuid(categoryUuid)
                .orElseThrow(DeletePowerOnNonExistingCategoryException::new);

		final Power power = PowerUtil.checkCategoryPowerForDelete(c, userId);

		c.removePower(power);

		final List<Subject> subjectsInCategory = subjectRepository.findSubjectByCategoryUuid(categoryUuid);
		for (Subject subject : subjectsInCategory) {
			try {
				Optional<Power> powerTmp = subject.findPower(userId);
				if (powerTmp.isPresent() && powerTmp.get().getCollaboratorIdTo().equals(power.getCollaboratorIdTo())) {
					deletePowerOnSubject(subject.getUuid());
				}
			} catch (DeletePowerOnNonExistingSubjectException | DeleteNonExistingPowerException | CloseSubjectException
			        | UserAlreadyVoteException e) {
				// if this appends, we just don't propagate power
				System.out.println("ERROR DELEGATION");
			}
		}

		categoryRepository.save(c);
	}
}
