package com.zenika.liquid.democracy.api.power.controller;

import com.zenika.liquid.democracy.api.exception.CloseSubjectException;
import com.zenika.liquid.democracy.api.power.exception.*;
import com.zenika.liquid.democracy.api.power.service.PowerService;
import com.zenika.liquid.democracy.api.vote.exception.UserAlreadyVoteException;
import com.zenika.liquid.democracy.model.Power;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/powers")
public class PowerController {

	private final PowerService powerService;

	@Autowired
	public PowerController(PowerService powerService) {
	    this.powerService = powerService;
    }

	@RequestMapping(method = RequestMethod.PUT, value = "/subjects/{subjectUuid}")
	public ResponseEntity<Void> addPowerOnSubject(@RequestBody Power p, @PathVariable String subjectUuid) {
		powerService.addPowerOnSubject(subjectUuid, p);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/categories/{categoryUuid}")
	public ResponseEntity<Void> addPowerOnCategory(@RequestBody Power p, @PathVariable String categoryUuid) {
		powerService.addPowerOnCategory(categoryUuid, p);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/subjects/{subjectUuid}")
	public ResponseEntity<Void> deletePowerOnSubject(@PathVariable String subjectUuid) {
		powerService.deletePowerOnSubject(subjectUuid);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/categories/{categoryUuid}")
	public ResponseEntity<Void> deletePowerOnCategory(@PathVariable String categoryUuid) {
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

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous avez déjà donné votre délégation")
	@ExceptionHandler(UserAlreadyGavePowerException.class)
	public void userAlreadyGavePowerHandler() {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cette délégation entraine une dépendance circulaire")
	@ExceptionHandler(CircularPowerDependencyException.class)
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
