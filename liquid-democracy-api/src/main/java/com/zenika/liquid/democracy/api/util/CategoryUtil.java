package com.zenika.liquid.democracy.api.util;

import org.apache.commons.lang3.StringUtils;

import com.zenika.liquid.democracy.api.exception.category.MalformedCategoryException;
import com.zenika.liquid.democracy.model.Category;

public class CategoryUtil {

	public static void checkCategory(Category c) throws MalformedCategoryException {

		if (StringUtils.isBlank(c.getTitle())) {
			throw new MalformedCategoryException();
		}

	}

}
