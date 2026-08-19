package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆详情信息模型
 */
@Data
public class TrafficVehicleDetailInfoModel {
    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 车辆型号
     */
    private String vehicleModel;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 基础状态 - 行驶里程（公里）
     */
    private Double mileage;

    /**
     * 基础状态 - 所属班组
     */
    private String team;

    /**
     * 运行成本 - 总费用
     */
    private Double totalCost;

    /**
     * 运行成本 - 月租金
     */
    private Double monthlyRent;

    /**
     * 运行成本 - 油费
     */
    private Double fuelCost;

    /**
     * 运行成本 - 燃油补差
     */
    private Double fuelSubsidy;

    /**
     * 运行成本 - 停车费
     */
    private Double parkingFee;

    /**
     * 运行成本 - 路桥费
     */
    private Double roadBridgeFee;

    /**
     * 运行安全 - 下次年审日期
     */
    private Date nextInspectDate;
}