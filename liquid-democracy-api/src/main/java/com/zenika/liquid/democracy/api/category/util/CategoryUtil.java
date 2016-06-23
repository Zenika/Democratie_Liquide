package com.zenika.liquid.democracy.api.category.util;

import org.apache.commons.lang3.StringUtils;

import com.zenika.liquid.democracy.api.category.exception.MalformedCategoryException;
import com.zenika.liquid.democracy.model.Category;

public class CategoryUtil {

	public static void checkCategory(Category c) throws MalformedCategoryException {

		if (StringUtils.isBlank(c.getTitle())) {
			throw new MalformedCategoryException();
		}

	}

}
