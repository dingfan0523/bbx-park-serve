
package com.cgnpc.bbxpark.common.utils;

import org.springframework.lang.Nullable;

import java.util.Arrays;


public class ObjectUtil extends org.springframework.util.ObjectUtils {

	/**
	 * 判断元素不为空
	 * @param obj object
	 * @return boolean
	 */
	public static boolean isNotEmpty(@Nullable Object obj) {
		return !ObjectUtil.isEmpty(obj);
	}

	@SafeVarargs
	public static <T> boolean equalsAny(T obj, T... array) {
		return Arrays.asList(array).contains(obj);
	}
}
