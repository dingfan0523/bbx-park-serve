package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模块类型
 */
@Getter
@AllArgsConstructor
public enum ModelTypeEnum {

    /**
     * 移动端
     */
    ZGHQ("zghq", "综管后勤"),

    CTHC("cthc", "餐厅后厨");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;


    public static String getCode(String name) {
        for (ModelTypeEnum configEnum : ModelTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (ModelTypeEnum configEnum : ModelTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}