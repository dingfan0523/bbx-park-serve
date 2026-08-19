package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 工单来源枚举
 * @author lhy
 * @date 2024/7/15 11:57
 */
@Getter
@AllArgsConstructor
public enum WorkOrderSourceEnum {
    /**
     *人工上报
     */
    PERSON("person", "报事报修"),

    /**
     *告警触发
     */
    ALARM( "alarm", "设备告警"),

    /**
     * 抄表计划
     */
    METERPLAN("meterPlan" , "抄表计划"),

    /**
     * 维保计划
     */
    MAINTAINPLAN("maintainPlan" , "维保计划"),

    /**
     * 巡检计划
     */
    INSPECTIONPLAN("inspectionPlan" , "巡检计划"),

    /**
     * 巡更计划
     */
    PATROLPLAN("patrolPlan" , "巡更计划"),
    /**
     * 盘点计划
     */
    INVENTORYPLAN("inventoryPlan" , "盘点计划"),
    /**
     * 任务计划
     */
    TASKPLAN("taskPlan" , "任务计划");

    /**
     * 状态编码
     */
    private final String code;

    /**
     * 状态名称
     */
    private final String name;

    public static String getCode(String name) {
        for (WorkOrderSourceEnum configEnum : WorkOrderSourceEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (WorkOrderSourceEnum configEnum : WorkOrderSourceEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}