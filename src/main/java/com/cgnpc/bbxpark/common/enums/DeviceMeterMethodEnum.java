package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 设备抄表方式枚举类
 * @author huangyongtao
 * @date 2025/4/21 10:35
 */
@Getter
@AllArgsConstructor
public enum DeviceMeterMethodEnum {

    /**
     * 人工抄表
     */
    PERSON("person", "人工抄表"),

    /**
     * 自动上报
     */
    AUTO("auto", "自动上报");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    public static String getCode(String name) {
        for (DeviceMeterMethodEnum configEnum : DeviceMeterMethodEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (DeviceMeterMethodEnum configEnum : DeviceMeterMethodEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}