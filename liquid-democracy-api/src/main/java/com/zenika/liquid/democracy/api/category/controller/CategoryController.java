package com.zenika.liquid.democracy.api.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.zenika.liquid.democracy.api.category.exception.MalformedCategoryException;
import com.zenika.liquid.democracy.api.category.exception.UnexistingCategoryException;
import com.zenika.liquid.democracy.api.category.service.CategoryService;
import com.zenika.liquid.democracy.model.Category;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> addCategory(@Validated @RequestBody Category c) throws MalformedCategoryException {

		Category out = categoryService.addCategory(c);

		return ResponseEntity.created(
		        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(out.getUuid()).toUri())
		        .build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Category>> getCategories() {

		List<Category> out = categoryService.getCategories();

		if (out.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(out);
		}

		return ResponseEntity.ok(out);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{categoryUuid}")
	public ResponseEntity<Category> getCategoryByUuid(@PathVariable String categoryUuid)
	        throws UnexistingCategoryException {

		Category c = categoryService.getCategoryByUuid(categoryUuid);

		return ResponseEntity.ok().body(c);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le titre est obligatoire")
	@ExceptionHandler(MalformedCategoryException.class)
	public void malformedCategoryHandler() {
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "La catégorie n'existe pas")
	@ExceptionHandler(UnexistingCategoryException.class)
	public void unexistingCategoryHandler() {
	}

}
