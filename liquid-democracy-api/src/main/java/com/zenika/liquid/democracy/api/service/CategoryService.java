package com.zenika.liquid.democracy.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.exception.category.MalformedCategoryException;
import com.zenika.liquid.democracy.api.exception.category.UnexistingCategoryException;
import com.zenika.liquid.democracy.model.Category;

@Service
public interface CategoryService {

	public Category addCategory(Category c) throws MalformedCategoryException;

	public List<Category> getCategories();

	public Category getCategoryByUuid(String categoryUuid) throws UnexistingCategoryException;

}
