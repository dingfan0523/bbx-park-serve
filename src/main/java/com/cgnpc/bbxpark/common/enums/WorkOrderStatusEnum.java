package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 工单状态枚举
 * @author lhy
 * @date 2024/7/15 11:57
 */
@Getter
@AllArgsConstructor
public enum WorkOrderStatusEnum {

    /**
     * 待分配
     */
    ALLOT(10, "待分配"),

    /**
     * 待处理
     */
    REPORTED(20, "待处理"),
    /**
     * 处理中
     */
    PROCESSING( 30, "处理中"),
    /**
     * 待审核
     */
    AUDIT( 40, "待审核"),
    /**
     * 已完成
     */
    COMPLETED( 50, "已完成");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (WorkOrderStatusEnum configEnum : WorkOrderStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (WorkOrderStatusEnum configEnum : WorkOrderStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}