package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

/**
 * 交通宏观指标模型
 * 包含总车辆数、总里程数、总维保次数等核心指标
 */
@Data
public class TrafficMacroIndexModel {
    /**
     * 总车辆数
     * 数据来源于车辆基础信息表的数据条数
     */
    private Long totalVehicles;

    /**
     * 总里程数（公里）
     * 数据来源于车辆费用统计表的约行驶公里数求和
     */
    private Double totalMileage;

    /**
     * 总维保次数
     * 数据来源于车辆维修记录表和车辆保养记录表中相应时间段的数据条数
     */
    private Long totalMaintenanceCount;
}