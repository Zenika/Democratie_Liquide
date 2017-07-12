package com.zenika.liquid.democracy.api.category.controller;

import com.zenika.liquid.democracy.api.category.exception.ExistingCategoryException;
import com.zenika.liquid.democracy.api.category.exception.MalformedCategoryException;
import com.zenika.liquid.democracy.api.category.exception.UnexistingCategoryException;
import com.zenika.liquid.democracy.api.category.service.CategoryService;
import com.zenika.liquid.democracy.dto.CategoryDto;
import com.zenika.liquid.democracy.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> addCategory(@Validated @RequestBody Category c) {
		CategoryDto out = categoryService.addCategory(c);
		return ResponseEntity.created(
		        ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
                        .buildAndExpand(out.getUuid()).toUri()
        ).build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoryDto>> getCategories() {
		List<CategoryDto> out = categoryService.getCategories();
		if (out.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(out);
		}
		return ResponseEntity.ok(out);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{categoryUuid}")
	public ResponseEntity<CategoryDto> getCategoryByUuid(@PathVariable String categoryUuid) {
		CategoryDto cdto = categoryService.getCategoryByUuid(categoryUuid);
		return ResponseEntity.ok().body(cdto);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le titre est obligatoire")
	@ExceptionHandler(MalformedCategoryException.class)
	public void malformedCategoryHandler() {
	}


	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Une catégorie du même nom existe déjà")
	@ExceptionHandler(ExistingCategoryException.class)
	public void existingCategoryHandler() {
	}


	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "La catégorie n'existe pas")
	@ExceptionHandler(UnexistingCategoryException.class)
	public void unexistingCategoryHandler() {
	}

}
