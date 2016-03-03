package com.zenika.liquid.democracy.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.zenika.Application;
import com.zenika.liquid.democracy.api.persistence.SubjectRepository;
import com.zenika.liquid.democracy.api.util.AuthenticationUtil;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;
import com.zenika.liquid.democracy.model.Vote;
import com.zenika.liquid.democracy.model.WeightedChoice;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("test-unitaire")
@PrepareForTest(AuthenticationUtil.class)
public class VoteControllerTest {

	@Rule
	public PowerMockRule rule = new PowerMockRule();

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
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(AuthenticationUtil.class);
		PowerMockito.when(AuthenticationUtil.getUserIdentifiant(Mockito.any(OAuth2Authentication.class)))
				.thenReturn("sandra.parlant@zenika.com");
	}

	@Test
	public void voteForSubjectTest() {

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

		Vote v = new Vote();
		WeightedChoice c1 = new WeightedChoice();
		c1.setPoints(1);
		c1.setProposition(p2);
		v.getChoices().add(c1);

		ResponseEntity<Object> addResp = template.exchange(
				"http://localhost:" + serverPort + "api/votes/" + s.getUuid(), HttpMethod.PUT, new HttpEntity<>(v),
				Object.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
	}

	@Test
	public void voteForSubjectAgainTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");

		Vote v = new Vote();
		v.setCollaborateurId("sandra.parlant@zenika.com");
		WeightedChoice c1 = new WeightedChoice();
		c1.setPoints(1);
		c1.setProposition(p2);
		v.getChoices().add(c1);
		s.getVotes().add(v);

		repository.save(s);

		ResponseEntity<Object> addResp = template.exchange(
				"http://localhost:" + serverPort + "api/votes/" + s.getUuid(), HttpMethod.PUT, new HttpEntity<>(v),
				Object.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("User has already voted"));
	}

	@Test
	public void voteForNonExistingSubjectTest() {
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

		Vote v = new Vote();
		WeightedChoice c1 = new WeightedChoice();
		c1.setPoints(1);
		c1.setProposition(p2);
		v.getChoices().add(c1);

		ResponseEntity<Object> addResp = template.exchange(
				"http://localhost:" + serverPort + "api/votes/" + s.getUuid() + 1, HttpMethod.PUT, new HttpEntity<>(v),
				Object.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("Subject doesn't exist"));
	}

	@Test
	public void voteForSubjectWithTooManyPointsTest() {
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

		Vote v = new Vote();
		WeightedChoice c1 = new WeightedChoice();
		c1.setPoints(2);
		c1.setProposition(p1);
		v.getChoices().add(c1);

		ResponseEntity<Object> addResp = template.exchange(
				"http://localhost:" + serverPort + "api/votes/" + s.getUuid(), HttpMethod.PUT, new HttpEntity<>(v),
				Object.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("Too many points used"));
	}

	@Test
	public void voteForSubjectWithUnexistingPropositionTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		s.getPropositions().add(p1);
		p1.setTitle("P1 title");
		repository.save(s);

		Proposition p2 = new Proposition();
		p2.setTitle("P2 title");

		Vote v = new Vote();
		WeightedChoice c1 = new WeightedChoice();
		c1.setPoints(1);
		c1.setProposition(p2);
		v.getChoices().add(c1);

		ResponseEntity<Object> addResp = template.exchange(
				"http://localhost:" + serverPort + "api/votes/" + s.getUuid(), HttpMethod.PUT, new HttpEntity<>(v),
				Object.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("Propositions voted are not correct"));
	}

	@Test
	public void voteForSubjectWithRepeatedPropositionTest() {
		Subject s = new Subject();
		s.setTitle("Title");
		s.setMaxPoints(2);
		s.setDescription("Description");
		Proposition p1 = new Proposition();
		Proposition p2 = new Proposition();
		s.getPropositions().add(p1);
		s.getPropositions().add(p2);
		p1.setTitle("P1 title");
		p2.setTitle("P2 title");
		repository.save(s);

		Vote v = new Vote();
		WeightedChoice c1 = new WeightedChoice();
		c1.setPoints(1);
		c1.setProposition(p1);

		WeightedChoice c2 = new WeightedChoice();
		c2.setPoints(1);
		c2.setProposition(p1);

		v.getChoices().add(c1);
		v.getChoices().add(c2);

		ResponseEntity<Object> addResp = template.exchange(
				"http://localhost:" + serverPort + "api/votes/" + s.getUuid(), HttpMethod.PUT, new HttpEntity<>(v),
				Object.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());
		assertEquals(true, addResp.getBody().toString().contains("Propositions voted are not correct"));
	}

}