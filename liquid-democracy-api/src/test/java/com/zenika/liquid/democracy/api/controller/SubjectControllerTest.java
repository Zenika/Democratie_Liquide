package com.zenika.liquid.democracy.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

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

import com.zenika.Application;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;

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

	@Test
	public void getSubjectsInProgressTest() {

		Subject l = new Subject();
		l.setTitle("Title");
		l.setDescription("Description");
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
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		l.getPropositions().add(p1);
		l.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		l = repository.save(l);

		ResponseEntity<Subject> addResp = template
				.getForEntity("http://localhost:" + serverPort + "api/subjects/" + l.getUuid(), Subject.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
		assertEquals(l.getUuid(), addResp.getBody().getUuid());

		addResp = template.getForEntity("http://localhost:" + serverPort + "api/subjects/" + l.getUuid() + 1,
				Subject.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.NOT_FOUND.value(), addResp.getStatusCode().value());
	}

}
