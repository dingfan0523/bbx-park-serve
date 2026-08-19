package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 车辆里程信息分页模型
 */
@Data
public class TrafficMileagePageModel {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 车辆类型
     * 如：大巴车、中巴车、商务车、轿车、电召车
     */
    private String vehicleType;

    /**
     * 当前行驶里程（公里）
     */
    private Double currentMileage;
}