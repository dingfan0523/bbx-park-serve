package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 车辆明细模型
 * 展示车辆档案信息及运行成本数据
 */
@Data
public class TrafficVehicleDetailModel {
    /**
     * 车辆ID
     */
    private String id;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 车辆类型
     * 如：商务车 / ~5-7年
     */
    private String vehicleType;


    /**
     * 总成本
     * 数据来源于费用结算表，展示当月数据
     */
    private Double totalCost;

    /**
     * 维保成本
     */
    private Double maintenanceCost;

    /**
     * 维保成本占比（%）
     * 计算公式：维保成本 / (维保成本 + 直接运营成本) × 100%
     */
    private Double maintenanceCostRate;

    /**
     * 诊断提醒
     * 维保成本占比≥15%：高耗低效
     * 10%≤维保成本占比＜15%：特别关注
     * 维保成本占比＜10%：正常
     */
    private String diagnosisReminder;
}