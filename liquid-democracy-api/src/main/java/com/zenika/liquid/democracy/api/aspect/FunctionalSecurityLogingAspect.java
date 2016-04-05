package com.zenika.liquid.democracy.api.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zenika.liquid.democracy.authentication.service.CollaboratorService;

@Aspect
@Component
public class FunctionalSecurityLogingAspect {

	private static final Logger LOG = LoggerFactory.getLogger(FunctionalSecurityLogingAspect.class);

	@Autowired
	private CollaboratorService collaboratorService;

	@Before("execution(* com.zenika.liquid.democracy.api.*.*.*(..))")
	public void beforeLogging(JoinPoint joinPoint) {
		String userId = collaboratorService.currentUser().getCollaboratorId();

		StringBuilder infos = new StringBuilder();
		infos.append("Starting ").append(joinPoint.getTarget().getClass()).append(" ")
				.append(joinPoint.getSignature().getName()).append(" by ").append(userId).append(" with arguments ");

		if (joinPoint.getArgs() != null && joinPoint.getArgs().length != 0) {
			for (Object args : joinPoint.getArgs()) {
				infos.append("\n \t \t").append(args);
			}
		} else {
			infos.append("no args");
		}

		LOG.info(infos.toString());
	}

	@After("execution(* com.zenika.liquid.democracy.api.*.*(..))")
	public void afterLogging(JoinPoint joinPoint) {
		LOG.info("Ending " + joinPoint.getTarget().getClass() + " " + joinPoint.getSignature().getName()
				+ " with arguments " + joinPoint.getArgs());
	}

}
