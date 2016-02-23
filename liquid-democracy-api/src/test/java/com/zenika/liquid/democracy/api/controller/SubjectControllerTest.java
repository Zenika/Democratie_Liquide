package com.zenika.liquid.democracy.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.liquid.democracy.api.Application;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;

import liquid.democracy.model.Proposition;
import liquid.democracy.model.Subject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("dev")
public class SubjectControllerTest {

	@Autowired
	SubjectRepository repository;

	@Value("${local.server.port}")
	private int serverPort;

	private RestTemplate template;

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception {
		template = new RestTemplate();
		template.setErrorHandler(new DefaultResponseErrorHandler() {
			protected boolean hasError(HttpStatus statusCode) {
				return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
			}
		});
		repository.deleteAll();
	}

	@Test
	public void addSubjectTest() {
		Subject l = new Subject();

		ResponseEntity<Void> addResp = template.postForEntity("http://localhost:" + serverPort + "api/subjects/", l,
				Void.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		l.setTitle("Title");
		addResp = template.postForEntity("http://localhost:" + serverPort + "api/subjects/", l, Void.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		l.setDescription("Description");
		addResp = template.postForEntity("http://localhost:" + serverPort + "api/subjects/", l, Void.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		l.getPropositions().add(p1);
		l.getPropositions().add(p2);
		addResp = template.postForEntity("http://localhost:" + serverPort + "api/subjects/", l, Void.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		addResp = template.postForEntity("http://localhost:" + serverPort + "api/subjects/", l, Void.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.CREATED.value(), addResp.getStatusCode().value());
	}

}
