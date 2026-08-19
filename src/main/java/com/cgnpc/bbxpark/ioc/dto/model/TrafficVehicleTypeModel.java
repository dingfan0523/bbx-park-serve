package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

/**
 * 车辆类型分布模型
 * 用于展示不同车型的数量统计
 */
@Data
public class TrafficVehicleTypeModel {
    /**
     * 车辆类型名称
     * 如：大巴车、中巴车、商务车、轿车、电召车
     */
    private String typeName;

    /**
     * 该类型车辆数量
     */
    private Integer count;

    /**
     * 占比百分比（%）
     */
    private Double percentage;
}