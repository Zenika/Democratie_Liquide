package com.zenika.liquid.democracy.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.power.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.exception.power.DeletePowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.power.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.exception.vote.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.service.PowerService;
import com.zenika.liquid.democracy.api.util.PowerUtil;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;

@Service
@EnableRetry
@Retryable(OptimisticLockingFailureException.class)
public class PowerServiceImpl implements PowerService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private CollaboratorService collaboratorService;

	public void addPowerOnSubject(String subjectUuid, Power power)
	        throws AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException {

		String userId = collaboratorService.currentUser().getEmail();

		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);
		if (!s.isPresent()) {
			throw new AddPowerOnNonExistingSubjectException();
		}

		boolean addVote = PowerUtil.checkPowerForAddition(power, s.get(), userId);

		PowerUtil.preparePower(power, s.get(), userId, addVote);

		subjectRepository.save(s.get());

	}

	public void deletePowerOnSubject(String subjectUuid) throws DeletePowerOnNonExistingSubjectException,
	        DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException {

		String userId = collaboratorService.currentUser().getEmail();

		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);
		if (!s.isPresent()) {
			throw new DeletePowerOnNonExistingSubjectException();
		}

		Power power = PowerUtil.checkPowerForDelete(s.get(), userId);

		s.get().removePower(power);

		subjectRepository.save(s.get());

	}
}
