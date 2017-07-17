package com.zenika.liquid.democracy.api.power.service;

import com.zenika.liquid.democracy.model.Power;
import org.springframework.stereotype.Service;

@Service
public interface PowerService {

	void addPowerOnSubject(String subjectUuid, Power power);

	void deletePowerOnSubject(String subjectUuid);

	void addPowerOnCategory(String categoryUuid, Power p);

	void deletePowerOnCategory(String categoryUuid);

}
