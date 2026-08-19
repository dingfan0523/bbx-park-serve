package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 人员类型枚举
 */
@Getter
@AllArgsConstructor
public enum PersonTypeEnum {

    /**
     * 访客
     */
    VISITOR("1", "访客"),

    /**
     * 员工
     */
    EMPLOYEE("2", "员工");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;


    public static String getCode(String name) {
        for (PersonTypeEnum configEnum : PersonTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (PersonTypeEnum configEnum : PersonTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}