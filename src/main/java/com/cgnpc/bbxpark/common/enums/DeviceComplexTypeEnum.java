package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 母子设备类型枚举类
 * @author huangyongtao
 * @date 2025/2/24 10:35
 */
@Getter
@AllArgsConstructor
public enum DeviceComplexTypeEnum {

    /**
     * 母设备
     */
    MOTHER(1, "母设备"),

    /**
     * 子设备
     */
    SON(2, "子设备");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (DeviceComplexTypeEnum configEnum : DeviceComplexTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (DeviceComplexTypeEnum configEnum : DeviceComplexTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}