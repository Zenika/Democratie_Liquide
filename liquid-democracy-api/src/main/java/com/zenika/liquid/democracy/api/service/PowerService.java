package com.zenika.liquid.democracy.api.service;

import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.exception.DeletePowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.model.Power;

@Service
public interface PowerService {

	public void addPowerOnSubject(String subjectUuid, Power power)
			throws AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
			UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException;

	public void deletePowerOnSubject(String subjectUuid) throws DeletePowerOnNonExistingSubjectException,
			DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException;

}
