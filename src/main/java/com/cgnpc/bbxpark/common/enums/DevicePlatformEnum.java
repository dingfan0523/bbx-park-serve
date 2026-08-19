package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 设备平台枚举类
 * @author huangyongtao
 * @date 2025/2/24 10:35
 */
@Getter
@AllArgsConstructor
public enum DevicePlatformEnum {

    /**
     * 非物联网设备
     */
    NO(0, "非物联网设备"),

    /**
     * 自有平台
     */
    OWN(1, "自有平台"),

    /**
     * 统建平台
     */
    UNIFIED(2, "统建平台"),

    /**
     * 安消平台
     */
    SECURE(3, "安消平台");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (DevicePlatformEnum configEnum : DevicePlatformEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (DevicePlatformEnum configEnum : DevicePlatformEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}