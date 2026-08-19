package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 设备类型枚举类
 * @author huangyongtao
 * @date 2025/2/24 10:35
 */
@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {

    /**
     * 单体设备
     */
    SIMPLE(1, "单体设备"),

    /**
     * 母子设备
     */
    COMPLEX(2, "母子设备");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (DeviceTypeEnum configEnum : DeviceTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (DeviceTypeEnum configEnum : DeviceTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}