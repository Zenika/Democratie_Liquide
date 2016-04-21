package com.zenika.liquid.democracy.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.category.MalformedCategoryException;
import com.zenika.liquid.democracy.api.exception.category.UnexistingCategoryException;
import com.zenika.liquid.democracy.api.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.service.CategoryService;
import com.zenika.liquid.democracy.api.util.CategoryUtil;
import com.zenika.liquid.democracy.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Category addCategory(Category c) throws MalformedCategoryException {

		CategoryUtil.checkCategory(c);

		return categoryRepository.save(c);
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
