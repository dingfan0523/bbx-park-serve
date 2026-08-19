package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通行结果枚举
 */
@Getter
@AllArgsConstructor
public enum AccessResultEnum {

    /**
     * 成功
     */
    SUCCESS("success", "成功"),

    /**
     * 失败
     */
    FAIL("fail", "失败");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;


    public static String getCode(String name) {
        for (AccessResultEnum configEnum : AccessResultEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (AccessResultEnum configEnum : AccessResultEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}