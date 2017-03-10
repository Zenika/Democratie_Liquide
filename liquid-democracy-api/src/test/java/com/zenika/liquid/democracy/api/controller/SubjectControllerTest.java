package com.zenika.liquid.democracy.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.zenika.liquid.democracy.dto.SubjectDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.zenika.liquid.democracy.Application;
import com.zenika.liquid.democracy.api.subject.persistence.SubjectRepository;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("test")
public class SubjectControllerTest {

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
	public void addSubjectTest() {
		Subject newSubject = new Subject();

		ResponseEntity<Void> addResp = template.postForEntity(
			"http://localhost:" + serverPort + "api/subjects/",
			newSubject,
			Void.class
		);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		newSubject.setTitle("Title");
		addResp = template.postForEntity(
			"http://localhost:" + serverPort + "api/subjects/",
			newSubject,
			Void.class
		);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		newSubject.setDescription("Description");
		newSubject.setCollaboratorId("sandra.parlant@zenika.com");
		addResp = template.postForEntity(
			"http://localhost:" + serverPort + "api/subjects/",
			newSubject,
			Void.class
		);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		newSubject.getPropositions().add(p1);
		newSubject.getPropositions().add(p2);
		addResp = template.postForEntity(
			"http://localhost:" + serverPort + "api/subjects/",
			newSubject,
			Void.class
		);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		addResp = template.postForEntity(
			"http://localhost:" + serverPort + "api/subjects/",
			newSubject,
			Void.class
		);
		assertNotNull(addResp);
		assertEquals(HttpStatus.CREATED.value(), addResp.getStatusCode().value());
	}

	@Test
	public void getSubjectsShouldReturnSubjectList() {
		Subject opened = new Subject();
		opened.setDeadLine(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
		opened.setTitle("Opened subject");
		opened.setDescription("Description");
		opened.setCollaboratorId("sandra.parlant@zenika.com");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		opened.getPropositions().add(p1);
		opened.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");

		Subject noDeadline = new Subject();
		noDeadline.setTitle("No deadline subject");
		noDeadline.setDescription("Description");
		noDeadline.setCollaboratorId("sandra.parlant@zenika.com");
		Proposition p3 = new Proposition();
		Proposition p4 = new Proposition();
		noDeadline.getPropositions().add(p3);
		noDeadline.getPropositions().add(p4);
		p3.setTitle("P2 title");
		p3.setTitle("P3 title");

		Subject closed = new Subject();
		opened.setDeadLine(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
		closed.setTitle("Title");
		closed.setDescription("Description");
		closed.setCollaboratorId("sandra.parlant@zenika.com");
		Proposition p5 = new Proposition();
		Proposition p6 = new Proposition();
		closed.getPropositions().add(p5);
		closed.getPropositions().add(p6);
		p3.setTitle("P5 title");
		p3.setTitle("P6 title");

		repository.save(Arrays.asList(opened, noDeadline, closed));

		final ResponseEntity<List> response = template.getForEntity(
				"http://localhost:" + serverPort + "api/subjects/",
				List.class
		);
		assertNotNull(response);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
		assertEquals(3, response.getBody().size());

	}

	@Test
	public void getSubjectsInProgressTest() {

		Subject l = new Subject();
		l.setTitle("Title");
		l.setDescription("Description");
		l.setCollaboratorId("sandra.parlant@zenika.com");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		l.getPropositions().add(p1);
		l.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		repository.save(l);

		ResponseEntity<List> addResp = template
				.getForEntity("http://localhost:" + serverPort + "api/subjects/inprogress", List.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
		assertEquals(1, addResp.getBody().size());

		Subject l2 = new Subject();
		l2.setTitle("Title");
		l2.setDescription("Description");
		l2.setCollaboratorId("sandra.parlant@zenika.com");
		Proposition p3 = new Proposition();
		Proposition p4 = new Proposition();
		l2.getPropositions().add(p3);
		l2.getPropositions().add(p4);
		p3.setTitle("P1 title");
		p4.setTitle("P2 title");
		Calendar d = Calendar.getInstance();
		d.add(Calendar.DAY_OF_MONTH, +2);
		l2.setDeadLine(d.getTime());
		repository.save(l2);

		addResp = template.getForEntity("http://localhost:" + serverPort + "api/subjects/inprogress", List.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
		assertEquals(2, addResp.getBody().size());

		Subject l3 = new Subject();
		l3.setTitle("Title");
		l3.setDescription("Description");
		l3.setCollaboratorId("sandra.parlant@zenika.com");
		Proposition p5 = new Proposition();
		Proposition p6 = new Proposition();
		l3.getPropositions().add(p5);
		l3.getPropositions().add(p6);
		p5.setTitle("P1 title");
		p6.setTitle("P2 title");
		d = Calendar.getInstance();
		d.add(Calendar.DAY_OF_MONTH, -1);
		l3.setDeadLine(d.getTime());
		repository.save(l3);

		addResp = template.getForEntity("http://localhost:" + serverPort + "api/subjects/inprogress", List.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
		assertEquals(2, addResp.getBody().size());
	}

	@Test
	public void getSubjectTest() {
		Subject l = new Subject();
		l.setTitle("Title");
		l.setDescription("Description");
		l.setCollaboratorId("sandra.parlant@zenika.com");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		l.getPropositions().add(p1);
		l.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		l = repository.save(l);

		ResponseEntity<SubjectDto> addResp = template
				.getForEntity("http://localhost:" + serverPort + "api/subjects/" + l.getUuid(), SubjectDto.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
		assertEquals(l.getUuid(), addResp.getBody().getUuid());

		addResp = template.getForEntity("http://localhost:" + serverPort + "api/subjects/" + l.getUuid() + 1,
				SubjectDto.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.NOT_FOUND.value(), addResp.getStatusCode().value());
	}

}
