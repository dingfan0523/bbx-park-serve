package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 设备级别枚举类
 * @author huangyongtao
 * @date 2025/2/24 10:35
 */
@Getter
@AllArgsConstructor
public enum DeviceLevelEnum {

    /**
     * 关键
     */
    KEY(10, "关键"),

    /**
     * 重要
     */
    IMPORTANT(20, "重要"),

    /**
     * 一般
     */
    GENERAL(30, "一般");


    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (DeviceLevelEnum configEnum : DeviceLevelEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (DeviceLevelEnum configEnum : DeviceLevelEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}