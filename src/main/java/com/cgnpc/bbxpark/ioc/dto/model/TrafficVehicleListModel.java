package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

/**
 * 车辆列表模型
 * 用于车辆类型分布点击后的车辆列表弹框展示
 */
@Data
public class TrafficVehicleListModel {
    /**
     * 车辆ID
     */
    private Long id;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 车辆类型
     * 如：大巴车、中巴车、商务车、轿车、电召车
     */
    private String vehicleType;

    /**
     * 车辆品牌
     */
    private String brand;

    /**
     * 车辆型号
     */
    private String model;

    /**
     * 购置日期
     */
    private String purchaseDate;

    /**
     * 车辆状态
     * 如：在用、维修中
     */
    private String status;

    /**
     * 使用部门
     */
    private String department;
}