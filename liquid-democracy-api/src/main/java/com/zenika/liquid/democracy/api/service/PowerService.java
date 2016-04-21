package com.zenika.liquid.democracy.api.service;

import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.power.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.exception.power.DeletePowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.power.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.exception.vote.UserAlreadyVoteException;
import com.zenika.liquid.democracy.model.Power;

@Service
public interface PowerService {

	public void addPowerOnSubject(String subjectUuid, Power power)
			throws AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
			UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException;

	public void deletePowerOnSubject(String subjectUuid) throws DeletePowerOnNonExistingSubjectException,
			DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException;

}
