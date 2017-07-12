package com.zenika.liquid.democracy.api.category.service;

import com.zenika.liquid.democracy.dto.CategoryDto;
import com.zenika.liquid.democracy.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

	CategoryDto addCategory(Category c);

	List<CategoryDto> getCategories();

	CategoryDto getCategoryByUuid(String categoryUuid);

}
