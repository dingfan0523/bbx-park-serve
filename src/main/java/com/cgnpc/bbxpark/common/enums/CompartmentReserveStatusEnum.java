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
public enum CompartmentReserveStatusEnum {

    /**
     * 已预定
     */
    RESERVED(10, "已预定"),

    /**
     * 已到店
     */
    ARRIVED( 20, "已到店）"),

    /**
     * 已过期
     */
    EXPIRED( 30, "已过期"),

    /**
     * 已取消
     */
    CANCELLED( 40, "已取消");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (CompartmentReserveStatusEnum configEnum : CompartmentReserveStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (CompartmentReserveStatusEnum configEnum : CompartmentReserveStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}