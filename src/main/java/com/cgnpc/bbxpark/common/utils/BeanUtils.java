package com.cgnpc.bbxpark.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

public class BeanUtils {
    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * .
     */
    private BeanUtils() {

    }

    /**
     * 获取bean中值不为空的属性.
     *
     * @param obj
     *            obj
     * @return Map<String, Object>
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> getBeanPropertysValue(Object obj) {

        Map<String, Object> valueMap = new HashMap<String, Object>();
        Class<?> cls = obj.getClass();
        List<Field> fields = new ArrayList<>(Arrays.asList(cls.getDeclaredFields()));
        if (null != cls.getSuperclass()) {
            Field[] superClassFields = cls.getSuperclass().getDeclaredFields();
            fields.addAll(Arrays.asList(superClassFields));
        }

        if (fields != null && fields.size() > 0) {
            for (Field tempField: fields) {
                if ("serialVersionUID".equals(tempField.getName())) {
                    continue;
                } else {
                    String methodName = "get"
                            + tempField.getName().substring(0, 1).toUpperCase()
                            + tempField.getName().substring(1);
                    Method method = null;
                    try {
                        method = cls.getMethod(methodName);
                    } catch (SecurityException e) {
                        LOGGER
                                .error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
                    } catch (NoSuchMethodException e) {
                        LOGGER
                                .error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
                    }
                    Object val = null;
                    try {
                        val = method.invoke(obj);
                    } catch (IllegalArgumentException e) {
                        LOGGER
                                .error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
                    } catch (IllegalAccessException e) {
                        LOGGER
                                .error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
                    } catch (InvocationTargetException e) {
                        LOGGER
                                .error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
                    }

                    if (val != null) {
                        valueMap.put(tempField.getName(), val);
                    }
                }
            }
        }

        return valueMap;
    }

    /**
     * 对象复制.
     *
     * @param source
     *            源对象
     * @param target
     *            目标对象
     */
    public static void copyProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    /**
     * 对象复制.
     *
     * @param source
     *            源对象
     * @param target
     *            目标对象
     * @throws BeansException 对象复制异常
     */
    public static void copyPropertiesExtend(Object source, Object target)
            throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(
                        source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();

                        if (!Modifier.isPublic(
                                readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod
                                    .getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Exception ex) {
                        throw new FatalBeanException(
                                "Could not copy properties[" + sourcePd.getName()
                                        + "] from source to target,",
                                ex);
                    }
                }
            }
        }
    }

    /**
     * 对象复制.
     *
     * @param source
     *            源对象
     * @param target
     *            目标对象
     * @param ignoreProperties 属性名
     * @throws BeansException 对象复制异常
     */
    public static void copyProperties(Object source, Object target,
                                      String... ignoreProperties) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null)
                ? Arrays.asList(ignoreProperties)
                : null;
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null && (ignoreProperties == null
                    || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(
                        source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();

                        if (!Modifier.isPublic(
                                readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod
                                    .getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Exception ex) {
                        throw new FatalBeanException(
                                "Could not copy properties[" + sourcePd.getName()
                                        + "] from source to target",
                                ex);
                    }
                }
            }
        }
    }

    /**
     * 对象转换.
     *
     * @param source
     *            需要转换的对象
     * @param target
     *            转换后的类型
     * @param <T>
     *            返回对象类型泛型
     * @return 返回对象
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes", "unchecked" })
    public static <T> T objConvert(Object source, Class<T> target) throws
            Exception {
        if (source == null) {
            throw new Exception("空记录");
        }
        T targets;
        try {
            targets = (T) target.newInstance(); // 创建目标对象实例
            Class<?> sourCls = source.getClass();
            // 遍历源属性
            do {
                Field[] sourFlds = sourCls.getDeclaredFields(); // 源属性集
                for (int i = 0; i < sourFlds.length; i++) { // 遍历源所有属性
                    Field sf = sourFlds[i];
                    sf.setAccessible(true);
                    // SystemType.out.println(sf.getName());
                    // 遍历目标所有属性
                    Class<?> toCls = target;
                    do {
                        Field[] toFlds = toCls.getDeclaredFields(); // 源属性集
                        for (int j = 0; j < toFlds.length; j++) { // 遍历源所有属性
                            Field tof = toFlds[j];
                            boolean isStatic = Modifier
                                    .isStatic(tof.getModifiers()); // 避免对静态字段进行设置值
                            if (isStatic) {
                                continue;
                            }
                            tof.setAccessible(true);
                            if (sf.getName().equals(tof.getName())) { // 属性名字相同
                                String type = tof.getType().toString(); // 得到此属性的类型
                                if (type.endsWith("String")) {
                                    tof.set(targets, (String) sf.get(source));
                                } else if (type.endsWith("int")
                                        || type.endsWith("Integer")) {
                                    tof.set(targets, (Integer) sf.get(source));
                                } else if (type.endsWith("Date")) {
                                    tof.set(targets, (Date) sf.get(source));
                                } else if (type.endsWith("long")
                                        || type.endsWith("Long")) {
                                    tof.set(targets, (Long) sf.get(source));
                                } else if (type.endsWith("short")
                                        || type.endsWith("Short")) {
                                    tof.set(targets, (Short) sf.get(source));
                                } else if (type.endsWith("double")
                                        || type.endsWith("Double")) {
                                    tof.set(targets, (Double) sf.get(source));
                                } else if (type.endsWith("boolean")
                                        || type.endsWith("Boolean")) {
                                    tof.set(targets, (Boolean) sf.get(source));
                                } else if (type.endsWith("BigDecimal")) {
                                    tof.set(targets, (BigDecimal) sf.get(source));
                                } else if (type.endsWith("List")) {
                                    // 判断list不为空
                                    if (sf.get(source) == null
                                            || ((List) sf.get(source))
                                            .size() == 0) {
                                        continue;
                                    }
                                    // 得到list
                                    List<Object> list = (List) sf.get(source);
                                    // 获得泛型
                                    Type fc = tof.getGenericType();
                                    if (fc == null) {
                                        continue;
                                    }
                                    List<Object> convertList = null;
                                    if (fc instanceof ParameterizedType) {
                                        // 获得泛型参数类型
                                        ParameterizedType pt = (ParameterizedType) fc;
                                        // 获得泛型的class
                                        Class<?> genericClazz = (Class<?>) pt
                                                .getActualTypeArguments()[0];

                                        convertList = new ArrayList<>();
                                        // 转换
                                        for (Object temp : list) {
                                            Object convertObj = objConvert(temp,
                                                    genericClazz);
                                            convertList.add(convertObj);
                                        }
                                    }
                                    tof.set(targets, convertList);

                                } else {
                                    System.out.println("类型转换失败！");
                                    throw new Exception("类型转换失败！");
                                }
                            }
                        }
                        toCls = toCls.getSuperclass();

                    } while (toCls != Object.class);
                }
                sourCls = sourCls.getSuperclass();
            } while (sourCls != Object.class);
        } catch (Exception e) {
            targets = null;
            e.printStackTrace();
        }
        return targets;
    }

    /**
     * 对象转换，根据mapping配置转换，忽略未配置的属性
     * .
     *
     * @param source
     *            原对象
     * @param target
     *            目标对象
     * @param mapping
     *            属性映射, key为原对象属性，value为目标对象属性
     * @param <T>
     *            返回对象类型泛型
     * @return 转换后的对象
     */
    public static <T> T objConvert(Object source, Class<T> target,
                                   Map<String, String> mapping) {
        if (source == null || mapping == null || mapping.size() == 0) {
            return null;
        }

        T targetObj = null;

        try {
            targetObj = target.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
        }
        Class<?> sourceClass = source.getClass();
        for (String sourceProp : mapping.keySet()) {
            try {
                // 从原对象中取出对应属性值
                Field sourceField = sourceClass.getDeclaredField(sourceProp);
                sourceField.setAccessible(true);
                Object sourceFieldObj = sourceField.get(source);
                if (sourceFieldObj == null) {
                    continue;
                }
                String value = sourceFieldObj.toString();
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                Field targetField = target
                        .getDeclaredField(mapping.get(sourceProp));
                targetField.setAccessible(true);
                Object valueObj = null;
                String type = targetField.getType().getName();
                if (type.endsWith("String")) {
                    valueObj = value;
                } else if (type.endsWith("long") || type.endsWith("Long")) {
                    valueObj = Long.valueOf(value);
                } else if (type.endsWith("int") || type.endsWith("Integer")) {
                    valueObj = Integer.valueOf(value);
                } else if (type.endsWith("Date")) {
                    valueObj = DateUtils.strToDateTime(value);
                } else if (type.endsWith("boolean")
                        || type.endsWith("Boolean")) {
                    valueObj = Boolean.valueOf(value);
                } else if (type.endsWith("byte") || type.endsWith("Byte")) {
                    valueObj = Byte.valueOf(value);
                } else if (type.endsWith("double") || type.endsWith("Double")) {
                    valueObj = Double.valueOf(value);
                } else if (type.endsWith("float") || type.endsWith("Float")) {
                    valueObj = Float.valueOf(value);
                } else if (type.endsWith("BigDecimal")) {
                    valueObj = new BigDecimal(value);
                } else {
                    throw new Exception("类型转换失败！");
                }
                targetField.set(targetObj, valueObj);
            } catch (NoSuchMethodException e) {
                LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
            } catch (SecurityException e) {
                LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
            } catch (IllegalAccessException e) {
                LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
            } catch (IllegalArgumentException e) {
                LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
            } catch (InvocationTargetException e) {
                LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
            } catch (NoSuchFieldException e) {
                LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
            } catch (Exception e) {
                LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
            }
        }
        return targetObj;
    }

    /**
     * map转为对象
     * .
     *
     * @param map
     *            待转换的map, key对应对象的属性
     * @param beanClass
     *            对象class
     * @param <T>
     *            返回对象类型泛型
     * @return 转换后的对象
     */
    public static <T> T mapConvertObj(Map<Object, Object> map,
                                      Class<T> beanClass) {
        if (map == null || map.size() == 0) {
            return null;
        }

        T obj = null;
        Class<?> c = beanClass;

        try {
            obj = beanClass.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
        }

        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (map.containsKey(field.getName())) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                field.setAccessible(true);
                try {
                    Object obj2 = map.get(field.getName());
                    if (obj2 == null) {
                        continue;
                    }
                    String value = obj2.toString();
                    Object valueObj = null;
                    String type = field.getType().getName();
                    if (type.endsWith("String")) {
                        valueObj = value;
                    } else if (type.endsWith("long") || type.endsWith("Long")) {
                        valueObj = Long.valueOf(value);
                    } else if (type.endsWith("int")
                            || type.endsWith("Integer")) {
                        valueObj = Integer.valueOf(value);
                    } else if (type.endsWith("Date")) {
                        valueObj = DateUtils.strToDateTime(value);
                    } else if (type.endsWith("boolean")
                            || type.endsWith("Boolean")) {
                        valueObj = Boolean.valueOf(value);
                    } else if (type.endsWith("byte") || type.endsWith("Byte")) {
                        valueObj = Byte.valueOf(value);
                    } else if (type.endsWith("double")
                            || type.endsWith("Double")) {
                        valueObj = Double.valueOf(value);
                    } else if (type.endsWith("float")
                            || type.endsWith("Float")) {
                        valueObj = Float.valueOf(value);
                    } else if (type.endsWith("BigDecimal")) {
                        valueObj = new BigDecimal(value);
                    } else {
                        valueObj = obj2;
                    }
                    field.set(obj, valueObj);
                } catch (IllegalArgumentException e) {
                    LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(),
                            e);
                } catch (IllegalAccessException e) {
                    LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(),
                            e);
                } catch (Exception e) {
                    LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(),
                            e);
                }
            }
        }

        return obj;
    }

    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier) {
        return convertTo(source, targetSupplier, null);
    }

    /**
     * 转换对象
     *
     * @param source         源对象
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
        if (null == source || null == targetSupplier) {
            return null;
        }

        T target = targetSupplier.get();
        copyProperties(source, target);
        if (callBack != null) {
            callBack.callBack(source, target);
        }
        return target;
    }

    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier) {
        return convertListTo(sources, targetSupplier, null);
    }

    /**
     * 转换对象
     *
     * @param sources        源对象list
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象list
     */
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
        if (null == sources || null == targetSupplier) {
            return null;
        }

        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T target = targetSupplier.get();
            copyProperties(source, target);
            if (callBack != null) {
                callBack.callBack(source, target);
            }
            list.add(target);
        }
        return list;
    }

    /**
     * 回调接口
     *
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     */
    @FunctionalInterface
    public interface ConvertCallBack<S, T> {
        void callBack(S t, T s);
    }
}
