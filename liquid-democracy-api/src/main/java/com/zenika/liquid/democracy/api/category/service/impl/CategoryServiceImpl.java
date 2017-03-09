package com.zenika.liquid.democracy.api.category.service.impl;

import com.zenika.liquid.democracy.api.category.exception.ExistingCategoryException;
import com.zenika.liquid.democracy.api.category.exception.MalformedCategoryException;
import com.zenika.liquid.democracy.api.category.exception.UnexistingCategoryException;
import com.zenika.liquid.democracy.api.category.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.category.service.CategoryService;
import com.zenika.liquid.democracy.api.category.util.CategoryUtil;
import com.zenika.liquid.democracy.config.MapperConfig;
import com.zenika.liquid.democracy.dto.CategoryDto;
import com.zenika.liquid.democracy.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    MapperConfig mapper;

    public CategoryDto addCategory(Category newCategory) throws MalformedCategoryException, ExistingCategoryException {

        // check category not blank
        CategoryUtil.checkCategory(newCategory);

        // find duplicates
        Boolean isPresent = categoryRepository.findAll().stream().anyMatch(p ->
                p.getTitle().toLowerCase().trim().equals(newCategory.getTitle().toLowerCase().trim())
        );
        if (isPresent) {
            throw new ExistingCategoryException();
        }

        return prepareCategoryForResponse(categoryRepository.save(newCategory));
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(c -> prepareCategoryForResponse(c)).collect(Collectors.toList());
    }

    public CategoryDto getCategoryByUuid(String categoryUuid) throws UnexistingCategoryException {
        Optional<Category> c = categoryRepository.findCategoryByUuid(categoryUuid);

        if (!c.isPresent()) {
            throw new UnexistingCategoryException();
        }

        return prepareCategoryForResponse(c.get());
    }

    private CategoryDto prepareCategoryForResponse(Category c) {
        return mapper.map(c, CategoryDto.class);
    }

}
