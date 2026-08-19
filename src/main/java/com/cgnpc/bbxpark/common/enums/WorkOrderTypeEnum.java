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
public enum WorkOrderTypeEnum {
    /**
     *报修工单
     */
    REPAIR("repair", "报修工单"),
    DEVICEALARM("deviceAlarm", "设备告警"),
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
        for (WorkOrderTypeEnum configEnum : WorkOrderTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (WorkOrderTypeEnum configEnum : WorkOrderTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}