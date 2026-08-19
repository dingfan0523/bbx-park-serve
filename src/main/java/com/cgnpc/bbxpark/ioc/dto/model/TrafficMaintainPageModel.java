package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆保养记录分页模型
 */
@Data
public class TrafficMaintainPageModel {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 车牌号
     */
    private String plateNum;

    /**
     * 保养日期
     */
    private Date maintainDate;

    /**
     * 保养费用
     */
    private Double itemAmount;

    /**
     * 保养项目
     */
    private String maintainItemName;

    /**
     * 上次保养日期
     */
    private Date lastMaintainDate;

    /**
     * 进厂里程数
     */
    private Double inMileage;

    /**
     * 保养厂
     */
    private String maintainLocation;

    /**
     * 车辆单位
     */
    private String vehicleDept;

    /**
     * 保养状态
     */
    private String maintainStatus;

    /**
     * 车辆类型
     */
    private String vehicleType;
}