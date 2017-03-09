package com.zenika.liquid.democracy.api.category.service;

import java.util.List;

import com.zenika.liquid.democracy.api.category.exception.ExistingCategoryException;
import com.zenika.liquid.democracy.dto.CategoryDto;
import org.springframework.stereotype.Service;

import com.zenika.liquid.democracy.api.category.exception.MalformedCategoryException;
import com.zenika.liquid.democracy.api.category.exception.UnexistingCategoryException;
import com.zenika.liquid.democracy.model.Category;

@Service
public interface CategoryService {

	public CategoryDto addCategory(Category c) throws MalformedCategoryException, ExistingCategoryException;

	public List<CategoryDto> getCategories();

	public CategoryDto getCategoryByUuid(String categoryUuid) throws UnexistingCategoryException;

}
