package com.zenika.liquid.democracy.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.zenika.liquid.democracy.Application;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.model.Power;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("test")
public class PowerControllerTest {

	@Autowired
	SubjectRepository repository;

	@Value("${local.server.port}")
	private int serverPort;

	private RestTemplate template;

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
	public void addPowerOnSubjectTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		repository.save(s);

		Power p = new Power();
		p.setCollaborateurIdTo("julie.bourhis@zenika.com");

		ResponseEntity<Object> addResp = template.exchange(
		        "http://localhost:" + serverPort + "api/powers/" + s.getUuid(), HttpMethod.PUT, new HttpEntity<>(p),
		        Object.class);

		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());

		Optional<Subject> savedSubject = repository.findSubjectByUuid(s.getUuid());
		assertEquals(true, savedSubject.isPresent());
		assertEquals(true, savedSubject.get().findPower("sandra.parlant@zenika.com").isPresent());
	}

	@Test
	public void addPowerOnSubjectAgainTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");

		Power p = new Power();
		p.setCollaborateurIdFrom("sandra.parlant@zenika.com");
		p.setCollaborateurIdTo("julie.bourhis@zenika.com");

		s.getPowers().add(p);
		repository.save(s);

		ResponseEntity<Object> addResp = template.exchange(
		        "http://localhost:" + serverPort + "api/powers/" + s.getUuid(), HttpMethod.PUT, new HttpEntity<>(p),
		        Object.class);

		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("UserAlreadyGavePowerException"));
	}

	@Test
	public void addPowerOnNonExistingSubjectTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		repository.save(s);

		Power p = new Power();
		p.setCollaborateurIdTo("julie.bourhis@zenika.com");

		ResponseEntity<Object> addResp = template.exchange(
		        "http://localhost:" + serverPort + "api/powers/" + s.getUuid() + 1, HttpMethod.PUT, new HttpEntity<>(p),
		        Object.class);

		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("AddPowerOnNonExistingSubjectException"));
	}

	@Test
	public void addPowerOnSubjectToHimselfTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		repository.save(s);

		Power p = new Power();
		p.setCollaborateurIdTo("sandra.parlant@zenika.com");

		ResponseEntity<Object> addResp = template.exchange(
		        "http://localhost:" + serverPort + "api/powers/" + s.getUuid(), HttpMethod.PUT, new HttpEntity<>(p),
		        Object.class);

		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("UserGivePowerToHimselfException"));
	}

	@Test
	public void deletePowerOnSubjectTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");

		Power p = new Power();
		p.setCollaborateurIdFrom("sandra.parlant@zenika.com");
		p.setCollaborateurIdTo("julie.bourhis@zenika.com");

		s.getPowers().add(p);
		repository.save(s);

		ResponseEntity<Object> addResp = template.exchange(
		        "http://localhost:" + serverPort + "api/powers/" + s.getUuid(), HttpMethod.DELETE,
		        new HttpEntity<>(null), Object.class);

		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
	}

	@Test
	public void deleteNonExistingPowerOnSubjectTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");

		Power p = new Power();
		p.setCollaborateurIdFrom("sandra.parlantt@zenika.com");
		p.setCollaborateurIdTo("julie.bourhis@zenika.com");

		s.getPowers().add(p);
		repository.save(s);

		ResponseEntity<Object> addResp = template.exchange(
		        "http://localhost:" + serverPort + "api/powers/" + s.getUuid(), HttpMethod.DELETE,
		        new HttpEntity<>(null), Object.class);

		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("DeleteNonExistingPowerException"));
	}

	@Test
	public void deletePowerOnNonExistingSubjectTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");

		Power p = new Power();
		p.setCollaborateurIdFrom("sandra.parlant@zenika.com");
		p.setCollaborateurIdTo("julie.bourhis@zenika.com");

		s.getPowers().add(p);
		repository.save(s);

		ResponseEntity<Object> addResp = template.exchange(
		        "http://localhost:" + serverPort + "api/powers/" + s.getUuid() + 1, HttpMethod.DELETE,
		        new HttpEntity<>(null), Object.class);

		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("DeletePowerOnNonExistingSubjectException"));
	}

}
