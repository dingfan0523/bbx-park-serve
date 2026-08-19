package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆维修记录分页模型
 */
@Data
public class TrafficRepairPageModel {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 车牌号
     */
    private String plateNum;

    /**
     * 维修厂
     */
    private String settlementParty;

    /**
     * 维修项目
     */
    private String repairItemName;

    /**
     * 申报日期
     */
    private Date applyDate;

    /**
     * 维修状态
     */
    private String repairStatus;

    /**
     * 车辆所属单位
     */
    private String vehicleDept;

    /**
     * 作业单号
     */
    private String workOrderNo;

    /**
     * 车辆类型
     */
    private String vehicleType;
}