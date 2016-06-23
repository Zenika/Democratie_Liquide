package com.zenika.liquid.democracy.api.power.service;

import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.AddPowerOnNonExistingCategoryException;
import com.zenika.liquid.democracy.api.power.exception.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.power.exception.DeletePowerOnNonExistingCategoryException;
import com.zenika.liquid.democracy.api.power.exception.DeletePowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.power.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.power.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.model.Power;

@Service
public interface PowerService {

	public void addPowerOnSubject(String subjectUuid, Power power)
	        throws AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException;

	public void deletePowerOnSubject(String subjectUuid) throws DeletePowerOnNonExistingSubjectException,
	        DeleteNonExistingPowerException, CloseSubjectException, UserAlreadyVoteException;

	public void addPowerOnCategory(String categoryUuid, Power p) throws AddPowerOnNonExistingCategoryException,
	        UserAlreadyGavePowerException, UserGivePowerToHimselfException;

	public void deletePowerOnCategory(String categoryUuid)
	        throws DeletePowerOnNonExistingCategoryException, DeleteNonExistingPowerException;

}
