package com.zenika.liquid.democracy.api.category.util;

import com.zenika.liquid.democracy.api.category.exception.MalformedCategoryException;
import com.zenika.liquid.democracy.model.Category;
import org.apache.commons.lang3.StringUtils;

public class CategoryUtil {

	public static void checkCategory(Category c) {
		if (StringUtils.isBlank(c.getTitle())) {
			throw new MalformedCategoryException();
		}
	}

}
