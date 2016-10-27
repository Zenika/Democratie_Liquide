package com.zenika.liquid.democracy.api.category.service;

import java.util.List;

import com.zenika.liquid.democracy.api.category.exception.ExistingCategoryException;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.category.exception.MalformedCategoryException;
import com.zenika.liquid.democracy.api.category.exception.UnexistingCategoryException;
import com.zenika.liquid.democracy.model.Category;

@Service
public interface CategoryService {

	public Category addCategory(Category c) throws MalformedCategoryException, ExistingCategoryException;

	public List<Category> getCategories();

	public Category getCategoryByUuid(String categoryUuid) throws UnexistingCategoryException;

}
