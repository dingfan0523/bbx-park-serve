package com.cgnpc.bbxpark.common.validation;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final String PHONE_NUMBER_PATTERN = "^1[35789]\\d{9}$|^((0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?)$";
    private final Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        // 初始化逻辑（如果有的话）
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return true;
        }
        return pattern.matcher(value).matches();
    }
}