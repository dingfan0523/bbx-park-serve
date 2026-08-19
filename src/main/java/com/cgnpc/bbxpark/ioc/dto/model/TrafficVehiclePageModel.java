package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆信息分页模型
 * 对应数据库表 dwd_vehicle_info
 */
@Data
public class TrafficVehiclePageModel {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 车型划分
     * 如：大巴车、中巴车、商务车、轿车、电召车
     */
    private String vehicleType;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 车辆型号
     */
    private String vehicleModel;

    /**
     * 车辆单位
     */
    private String department;

    /**
     * 车辆使用状态
     * 如：在用、停用、维修中
     */
    private String useStatus;

    /**
     * 车辆运行状态
     * 如：运行中、空闲、故障
     */
    private String runStatus;

    /**
     * 行驶里程（公里）
     */
    private BigDecimal mileage;

    /**
     * 下次年审日期
     */
    private Date nextInspectDate;

    /**
     * 数据导入时间
     */
    private Date importTime;
}