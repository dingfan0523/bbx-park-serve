package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 包间预约状态枚举
 * @author huangyongtao
 * @date 2024/7/31 15:35
 */
@Getter
@AllArgsConstructor
public enum CompartmentReserveTypeEnum {

    /**
     * 移动端
     */
    APP("app", "移动端"),

    /**
     * 移动端
     */
    WEB( "web", "移动端");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    public static String getCode(String name) {
        for (CompartmentReserveTypeEnum configEnum : CompartmentReserveTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (CompartmentReserveTypeEnum configEnum : CompartmentReserveTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}