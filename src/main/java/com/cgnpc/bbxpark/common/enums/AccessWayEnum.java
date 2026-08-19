package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通行方式枚举
 */
@Getter
@AllArgsConstructor
public enum AccessWayEnum {

    /**
     * 刷卡
     */
    CARD("card", "刷卡"),

    /**
     * 刷脸
     */
    FACE("face", "刷脸");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;


    public static String getCode(String name) {
        for (AccessWayEnum configEnum : AccessWayEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (AccessWayEnum configEnum : AccessWayEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}