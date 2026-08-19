package com.cgnpc.bbxpark.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.cgnpc.bbxpark.common.base.IResultCode;
import com.cgnpc.cud.core.exception.BaseException;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.lang.Objects;
import io.jsonwebtoken.lang.Strings;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

public class AssertUtils {
    /**
     * 表达式 false 抛出异常提示
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BaseException(message);
        }
    }

//    public static void isTrue(boolean expression, String message, Runnable failCallback) {
//        if (!expression) {
//            throw new BaseException(message);
//        }
//    }
//
    /**
     * 表达式 false 抛出异常提示
     */
    public static void isTrue(boolean expression, IResultCode resultCode) {
        if (!expression) {
//            throw new BaseException(resultCode);
            throw new BaseException(null,resultCode.getCode() + "",null,resultCode.getMessage());
        }
    }

    /**
     * 不为null 抛出异常提示
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new BaseException(message);
        }
    }

    public static void ObjIsNotEmpty(Object object, String message) {
        if (ObjectUtil.isNotEmpty(object)) {
            throw new BaseException(message);
        }
    }

    /**
     * 不为null 抛出异常提示
     */
//    public static void isNull(Object object, IResultCode resultCode) {
//        if (object != null) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * 为null 抛出异常提示
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * 为null 抛出异常提示
     */
    public static void notNull(Object object, IResultCode resultCode) {
        if (object == null) {
//            throw new BaseException(resultCode);
            throw new BaseException(null,resultCode.getCode() + "",null,resultCode.getMessage());
        }
    }

    public static void notBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * 长度为0 抛出异常提示
     * null -> 异常
     * “” -> 异常
     * “ ” -> 正常
     */
    public static void hasLength(String text, String message) {
        if (!Strings.hasLength(text)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * 长度为0 抛出异常提示
     * null -> 异常
     * “” -> 异常
     * “ ” -> 正常
     */
//    public static void hasLength(String text, IResultCode resultCode) {
//        if (!Strings.hasLength(text)) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * 空字符串抛出异常提示
     * null -> 异常
     * “” -> 异常
     * “ ” -> 异常
     * “123” -> 正常
     */
    public static void hasText(String text, String message) {
        if (!Strings.hasText(text)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * 空字符串抛出异常提示
     * null -> 异常
     * “” -> 异常
     * “ ” -> 异常
     * “123” -> 正常
     */
//    public static void hasText(String text, IResultCode resultCode) {
//        if (!Strings.hasText(text)) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * textToSearch 无长度或者 不包含 substring 异常提示
     */
    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (Strings.hasLength(textToSearch) && Strings.hasLength(substring) && textToSearch.indexOf(substring) != -1) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * textToSearch 无长度或者 不包含 substring 异常提示
     */
//    public static void doesNotContain(String textToSearch, String substring, IResultCode resultCode) {
//        if (Strings.hasLength(textToSearch) && Strings.hasLength(substring) && textToSearch.indexOf(substring) != -1) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * Object数组为空异常提示
     */
    public static void notEmpty(Object[] array, String message) {
        if (Objects.isEmpty(array)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * Object数组为空异常提示
     */
//    public static void notEmpty(Object[] array, IResultCode resultCode) {
//        if (Objects.isEmpty(array)) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * byte数组为空异常提示
     */
    public static void notEmpty(byte[] array, String message) {
        if (Objects.isEmpty(array)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * byte数组为空异常提示
     */
//    public static void notEmpty(byte[] array, IResultCode resultCode) {
//        if (Objects.isEmpty(array)) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * 数据内有null异常提示
     */
    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
//                    throw new BaseException(message);
                    throw new BaseException(message);
                }
            }
        }
    }

    /**
     * 数据内有null异常提示
     */
//    public static void noNullElements(Object[] array, IResultCode resultCode) {
//        if (array != null) {
//            for (int i = 0; i < array.length; i++) {
//                if (array[i] == null) {
//                    throw new BaseException(resultCode);
//                }
//            }
//        }
//    }

    /**
     * 集合为空异常提示
     */
    public static void notEmpty(Collection collection, String message) {
        if (Collections.isEmpty(collection)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * 支持String,Collection,Optional,Object[]
     */
    public static void isEmpty(Object obj, String message) {
        if (Func.isNotEmpty(obj)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    public static void isEmpty(Object obj, IResultCode resultCode) {
        if (Func.isNotEmpty(obj)) {
//            throw new BaseException(resultCode);
            throw new BaseException(null,resultCode.getCode() + "",null,resultCode.getMessage());
        }
    }

    /**
     * 支持String,Collection,Optional,Object[]
     */
    public static void isNotEmpty(Object obj, String message) {
        if (Func.isEmpty(obj)) {
            throw new BaseException(message);
        }
    }

    public static void isNotEmpty(Object obj, IResultCode resultCode) {
        if (Func.isEmpty(obj)) {
            throw new BaseException(null,resultCode.getCode() + "",null,resultCode.getMessage());
        }
    }

    /**
     * 集合为空异常提示
     */
    public static void notEmpty(Collection collection, IResultCode resultCode) {
        if (Collections.isEmpty(collection)) {
            throw new BaseException(null,resultCode.getCode() + "",null,resultCode.getMessage());
        }
    }

    /**
     * map为空异常提示
     */
    public static void notEmpty(Map map, String message) {
        if (Collections.isEmpty(map)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * map为空异常提示
     */
//    public static void notEmpty(Map map, IResultCode resultCode) {
//        if (Collections.isEmpty(map)) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * 数据类型不匹配 异常提示
     */
    public static void isInstanceOf(Class type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * 数据类型不匹配 异常提示
     */
//    public static void isInstanceOf(Class type, Object obj, IResultCode resultCode) {
//        notNull(type, "Type to check against must not be null");
//        if (!type.isInstance(obj)) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * 非继承关系 异常提示
     */
    public static void isAssignable(Class superType, Class subType, String message) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * 非继承关系 异常提示
     */
//    public static void isAssignable(Class superType, Class subType, IResultCode resultCode) {
//        notNull(superType, "Type to check against must not be null");
//        if (subType == null || !superType.isAssignableFrom(subType)) {
//            throw new BaseException(resultCode);
//        }
//    }

    /**
     * 表达式为false 异常提示
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

    /**
     * 表达式为false 异常提示
     */
//    public static void state(boolean expression, IResultCode resultCode) {
//        if (!expression) {
//            throw new BaseException(resultCode);
//        }
//    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

//    public static void isFalse(boolean expression, IResultCode resultCode) {
//        if (expression) {
//            throw new BaseException(resultCode);
//        }
//    }

    public static void isEquals(Object o1, Object o2, String message) {
        if (!java.util.Objects.equals(o1, o2)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

//    public static void isEquals(Object o1, Object o2, IResultCode resultCode) {
//        if (!java.util.Objects.equals(o1, o2)) {
//            throw new BaseException(resultCode);
//        }
//    }

    public static void isNotEquals(Object o1, Object o2, String message) {
        if (java.util.Objects.equals(o1, o2)) {
//            throw new BaseException(message);
            throw new BaseException(message);
        }
    }

//    public static void isNotEquals(Object o1, Object o2, IResultCode resultCode) {
//        if (java.util.Objects.equals(o1, o2)) {
//            throw new BaseException(resultCode);
//        }
//    }
}
