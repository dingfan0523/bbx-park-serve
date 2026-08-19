package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 设备抄表类型枚举类
 * @author huangyongtao
 * @date 2025/4/14 10:35
 */
@Getter
@AllArgsConstructor
public enum DeviceReadingTypeEnum {

    /**
     * 水表
     */
    WATER("water", "水表"),

    /**
     * 燃气表
     */
    GAS("gas", "燃气表"),

    /**
     * 电表
     */
    ELECTRICITY("electricity", "电表");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    public static String getCode(String name) {
        for (DeviceReadingTypeEnum configEnum : DeviceReadingTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (DeviceReadingTypeEnum configEnum : DeviceReadingTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}