package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通行方向枚举
 */
@Getter
@AllArgsConstructor
public enum AccessDirEnum {

    /**
     * 进
     */
    IN("in", "进"),

    /**
     * 出
     */
    OUT("out", "出");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;


    public static String getCode(String name) {
        for (AccessDirEnum configEnum : AccessDirEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (AccessDirEnum configEnum : AccessDirEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}