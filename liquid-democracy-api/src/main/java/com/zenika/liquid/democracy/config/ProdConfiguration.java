package com.zenika.liquid.democracy.config;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zenika.liquid.democracy.authentication.service.CollaboratorService;

@Configuration
@Profile({ "docker", "test-prod" })
public class ProdConfiguration {

	private static final String PATTERN = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] boot%X{context} - ${PID} %5p [%t] --- %c{3}: %m%n";
	private static final String DATE_PATTERN = "yyyy-MM-dd";

	@Bean
	public CollaboratorService collaboratorService() {
		return new CollaboratorService();
	}

	public ConsoleAppender consoleAppender() {
		PatternLayout patternLayout = new PatternLayout(PATTERN);
		ConsoleAppender consoleAppender = new ConsoleAppender(patternLayout);
		consoleAppender.setThreshold(Level.ALL);
		return consoleAppender;
	}

	public DailyRollingFileAppender apiFileAppender() throws IOException {
		PatternLayout patternLayout = new PatternLayout(PATTERN);

		DailyRollingFileAppender apiFileAppender = new DailyRollingFileAppender(patternLayout,
				"./logs/liquid_democracy_api.log", DATE_PATTERN);
		apiFileAppender.setThreshold(Level.ALL);

		return apiFileAppender;
	}

	public DailyRollingFileAppender applicationFileAppender() throws IOException {
		PatternLayout patternLayout = new PatternLayout(PATTERN);

		DailyRollingFileAppender apiFileAppender = new DailyRollingFileAppender(patternLayout,
				"./logs/liquid_democracy_application.log", DATE_PATTERN);
		apiFileAppender.setThreshold(Level.ERROR);

		return apiFileAppender;
	}

	@Bean
	public Logger apiLogger() throws IOException {
		Logger apiLogger = Logger.getLogger("com.zenika.liquid.democracy.api");
		apiLogger.setLevel(Level.INFO);
		apiLogger.addAppender(apiFileAppender());
		apiLogger.addAppender(consoleAppender());

		return apiLogger;
	}

	@Bean
	public Logger springLogger() throws IOException {
		Logger apiLogger = Logger.getLogger("org.springframework");
		apiLogger.setLevel(Level.INFO);
		apiLogger.addAppender(applicationFileAppender());
		apiLogger.addAppender(consoleAppender());

		return apiLogger;
	}

}
