package com.zenika.liquid.democracy.api.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import com.zenika.liquid.democracy.Application;
import com.zenika.liquid.democracy.api.category.persistence.CategoryRepository;
import com.zenika.liquid.democracy.model.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("test")
public class CategoryControllerTest {

	@Autowired
	CategoryRepository repository;

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
	public void addCategoryTest() {
		Category c = new Category();

		ResponseEntity<Void> addResp = template.postForEntity("http://localhost:" + serverPort + "api/categories/", c,
		        Void.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.BAD_REQUEST.value(), addResp.getStatusCode().value());

		c.setTitle("Title");
		addResp = template.postForEntity("http://localhost:" + serverPort + "api/categories/", c, Void.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.CREATED.value(), addResp.getStatusCode().value());
	}

	@Test
	public void getCategoriesTest() {

		Category c = new Category();
		c.setTitle("Title");
		c.setDescription("Description");
		repository.save(c);

		ResponseEntity<List> addResp = template.getForEntity("http://localhost:" + serverPort + "api/categories",
		        List.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
		assertEquals(1, addResp.getBody().size());

		Category c2 = new Category();
		c2.setTitle("Title");
		c2.setDescription("Description");
		repository.save(c2);

		addResp = template.getForEntity("http://localhost:" + serverPort + "api/categories", List.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
		assertEquals(2, addResp.getBody().size());
	}

	@Test
	public void getCategoryTest() {

		Category c = new Category();
		c.setTitle("Title");
		c.setDescription("Description");
		repository.save(c);

		ResponseEntity<Category> addResp = template
		        .getForEntity("http://localhost:" + serverPort + "api/categories/" + c.getUuid(), Category.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.OK.value(), addResp.getStatusCode().value());
		assertEquals(c.getUuid(), addResp.getBody().getUuid());

		addResp = template.getForEntity("http://localhost:" + serverPort + "api/categories/" + c.getUuid() + 1,
		        Category.class);
		assertNotNull(addResp);
		assertEquals(HttpStatus.NOT_FOUND.value(), addResp.getStatusCode().value());
	}

}
