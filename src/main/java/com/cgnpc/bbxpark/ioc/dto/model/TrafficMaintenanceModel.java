package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 车辆维护情况模型
 * 按月份统计直接运营成本和维保成本
 */
@Data
public class TrafficMaintenanceModel {
    /**
     * 月份（格式：yyyy年MM月）
     */
    private String month;

    /**
     * 直接运营成本
     * 车辆费用统计表中，所有车辆月租金+油费+燃油补差+停车费+路桥费求和
     */
    private Double directOperatingCost;

    /**
     * 维保成本
     * 保养记录表的保养费用求和 + 维修记录表的维修费用求和 + 轮胎更换记录表的费用求和
     */
    private Double maintenanceCost;

    /**
     * 总成本（直接运营成本 + 维保成本）
     */
    private Double totalCost;
}