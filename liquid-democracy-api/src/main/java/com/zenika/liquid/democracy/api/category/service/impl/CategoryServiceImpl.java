package com.zenika.liquid.democracy.api.category.service.impl;

import java.util.List;
import java.util.Optional;

import com.zenika.liquid.democracy.api.category.exception.ExistingCategoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.category.exception.MalformedCategoryException;
import com.zenika.liquid.democracy.api.category.exception.UnexistingCategoryException;
import com.zenika.liquid.democracy.api.category.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.category.service.CategoryService;
import com.zenika.liquid.democracy.api.category.util.CategoryUtil;
import com.zenika.liquid.democracy.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Category addCategory(Category newCategory) throws MalformedCategoryException, ExistingCategoryException {

		// check category not blank
		CategoryUtil.checkCategory(newCategory);

		// find duplicates
		Boolean isPresent = categoryRepository.findAll().stream().anyMatch(p ->
			p.getTitle().toLowerCase().trim().equals(newCategory.getTitle().toLowerCase().trim())
		);
		if (isPresent) {
			throw new ExistingCategoryException();
		}


		return categoryRepository.save(newCategory);
	}

	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}

	public Category getCategoryByUuid(String categoryUuid) throws UnexistingCategoryException {
		Optional<Category> c = categoryRepository.findCategoryByUuid(categoryUuid);

		if (!c.isPresent()) {
			throw new UnexistingCategoryException();
		}

		return c.get();
	}

}
