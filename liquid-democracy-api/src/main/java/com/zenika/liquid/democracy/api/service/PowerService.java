package com.zenika.liquid.democracy.api.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.exception.DeletePowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.util.PowerUtil;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.si.core.zenika.authentication.service.CollaboratorService;

@Service
public class PowerService {

	private static final Logger LOG = LoggerFactory.getLogger(PowerService.class);

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private CollaboratorService collaboratorService;

	public void addPowerOnSubject(String subjectUuid, Power power)
			throws AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
			UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException {
		String userId = collaboratorService.currentUser().getCollaboratorId();

		LOG.info("Trying to add power for subject {} with {}", subjectUuid, power);

		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);
		if (!s.isPresent()) {
			throw new AddPowerOnNonExistingSubjectException();
		}

		LOG.info("Checking power for subject {} with {}", subjectUuid, power);
		PowerUtil.checkPowerForAddition(power, s.get(), userId);

		LOG.info("Preparing power for subject {} with {}", subjectUuid, power);
		PowerUtil.preparePower(power, s.get(), userId);

		LOG.info("Adding power for subject {} with {}", subjectUuid, power);
		subjectRepository.save(s.get());

	}

	public void deletePowerOnSubject(String subjectUuid) throws DeletePowerOnNonExistingSubjectException,
			DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException {
		String userId = collaboratorService.currentUser().getCollaboratorId();

		LOG.info("Trying to delete power for subject {} for {}", subjectUuid, userId);

		Optional<Subject> s = subjectRepository.findSubjectByUuid(subjectUuid);
		if (!s.isPresent()) {
			throw new DeletePowerOnNonExistingSubjectException();
		}

		LOG.info("Checking power for subject {} for {}", subjectUuid, userId);
		Power power = PowerUtil.checkPowerForDelete(s.get(), userId);

		s.get().removePower(power);

		LOG.info("Removing power {} for subject {} for {}", power, subjectUuid, userId);
		subjectRepository.save(s.get());
	}
}
