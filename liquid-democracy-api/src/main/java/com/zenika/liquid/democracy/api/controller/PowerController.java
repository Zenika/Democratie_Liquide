package com.zenika.liquid.democracy.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.zenika.liquid.democracy.api.exception.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.exception.DeletePowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.exception.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.service.PowerService;
import com.zenika.liquid.democracy.model.Power;

@RestController
@RequestMapping("/api/powers")
public class PowerController {

	private static final Logger LOG = LoggerFactory.getLogger(PowerController.class);

	@Autowired
	private PowerService powerService;

	@RequestMapping(method = RequestMethod.PUT, value = "/{subjectUuid}")
	public ResponseEntity<Void> addPowerOnSubject(@RequestBody Power p, @PathVariable String subjectUuid)
			throws AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
			UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException {

		LOG.info("addPowerOnSubject {} ", p);

		powerService.addPowerOnSubject(subjectUuid, p);

		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{subjectUuid}")
	public ResponseEntity<Void> deletePowerOnSubject(@PathVariable String subjectUuid)
			throws DeletePowerOnNonExistingSubjectException, DeleteNonExistingPowerException, CloseSubjectException,
			UserAlreadyVoteException {

		LOG.info("deletePowerOnSubject");

		powerService.deletePowerOnSubject(subjectUuid);

		return ResponseEntity.ok().build();
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Subject doesn't exist")
	@ExceptionHandler({ AddPowerOnNonExistingSubjectException.class, DeletePowerOnNonExistingSubjectException.class })
	public void addPowerOnNonExistingSubjectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User has already given his power")
	@ExceptionHandler(UserAlreadyGavePowerException.class)
	public void userAlreadyGavePowerHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User gave his power to himself")
	@ExceptionHandler(UserGivePowerToHimselfException.class)
	public void userGivePowerToHimselfHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User hasn't given any power on this subject")
	@ExceptionHandler(DeleteNonExistingPowerException.class)
	public void deleteNonExistingPowerHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Subject is closed")
	@ExceptionHandler(CloseSubjectException.class)
	public void managePowerOnClosedSubjectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "You or the other collaborator had already voted")
	@ExceptionHandler(UserAlreadyVoteException.class)
	public void userAlreadyVoteHandler() {
	}

}
