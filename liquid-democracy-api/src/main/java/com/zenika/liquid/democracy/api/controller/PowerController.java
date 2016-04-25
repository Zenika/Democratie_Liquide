package com.zenika.liquid.democracy.api.controller;

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

import com.zenika.liquid.democracy.api.exception.commons.CloseSubjectException;
import com.zenika.liquid.democracy.api.exception.power.AddPowerOnNonExistingCategoryException;
import com.zenika.liquid.democracy.api.exception.power.AddPowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.DeleteNonExistingPowerException;
import com.zenika.liquid.democracy.api.exception.power.DeletePowerOnNonExistingCategoryException;
import com.zenika.liquid.democracy.api.exception.power.DeletePowerOnNonExistingSubjectException;
import com.zenika.liquid.democracy.api.exception.power.UserAlreadyGavePowerException;
import com.zenika.liquid.democracy.api.exception.power.UserGivePowerToHimselfException;
import com.zenika.liquid.democracy.api.exception.vote.UserAlreadyVoteException;
import com.zenika.liquid.democracy.api.service.PowerService;
import com.zenika.liquid.democracy.model.Power;

@RestController
@RequestMapping("/api/powers")
public class PowerController {

	@Autowired
	private PowerService powerService;

	@RequestMapping(method = RequestMethod.PUT, value = "/subjects/{subjectUuid}")
	public ResponseEntity<Void> addPowerOnSubject(@RequestBody Power p, @PathVariable String subjectUuid)
	        throws AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException {

		powerService.addPowerOnSubject(subjectUuid, p);

		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/categories/{categoryUuid}")
	public ResponseEntity<Void> addPowerOnCategory(@RequestBody Power p, @PathVariable String categoryUuid)
	        throws AddPowerOnNonExistingSubjectException, UserAlreadyGavePowerException,
	        UserGivePowerToHimselfException, CloseSubjectException, UserAlreadyVoteException,
	        AddPowerOnNonExistingCategoryException {

		powerService.addPowerOnCategory(categoryUuid, p);

		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/subjects/{subjectUuid}")
	public ResponseEntity<Void> deletePowerOnSubject(@PathVariable String subjectUuid)
	        throws DeletePowerOnNonExistingSubjectException, DeleteNonExistingPowerException, CloseSubjectException,
	        UserAlreadyVoteException {

		powerService.deletePowerOnSubject(subjectUuid);

		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/categories/{categoryUuid}")
	public ResponseEntity<Void> deletePowerOnCategory(@PathVariable String categoryUuid)
	        throws DeletePowerOnNonExistingCategoryException, DeleteNonExistingPowerException {

		powerService.deletePowerOnCategory(categoryUuid);

		return ResponseEntity.ok().build();
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le sujet n'existe pas")
	@ExceptionHandler({ AddPowerOnNonExistingSubjectException.class, DeletePowerOnNonExistingSubjectException.class })
	public void addPowerOnNonExistingSubjectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "La catégorie n'existe pas")
	@ExceptionHandler({ AddPowerOnNonExistingCategoryException.class, DeletePowerOnNonExistingCategoryException.class })
	public void addPowerOnNonExistingCategoryHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous ou la personne avez déjà donné votre délégation")
	@ExceptionHandler(UserAlreadyGavePowerException.class)
	public void userAlreadyGavePowerHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous ne pouvez pas donner délégation à vous-même")
	@ExceptionHandler(UserGivePowerToHimselfException.class)
	public void userGivePowerToHimselfHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "La délégation n'existe pas")
	@ExceptionHandler(DeleteNonExistingPowerException.class)
	public void deleteNonExistingPowerHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le sujet est terminé")
	@ExceptionHandler(CloseSubjectException.class)
	public void managePowerOnClosedSubjectHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous ou la personne avez déjà voté")
	@ExceptionHandler(UserAlreadyVoteException.class)
	public void userAlreadyVoteHandler() {
	}

}
