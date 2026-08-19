package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆维修信息表实体类
 * 对应数据库表: MATRIX.dwd_vehicle_repair
 */
@Data
@TableName("dwd_vehicle_repair")
public class DwdVehicleRepair implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /** 车牌号 */
    private String plateNum;

    /** 车辆所属单位 */
    private String vehicleDept;

    /** 作业单号 */
    private String workOrderNo;

    /** 派工员 */
    private String dispatcher;

    /** 报修人 */
    private String reporter;

    /** 申报日期 */
    private Date applyDate;

    /** 维修状态 */
    private String repairStatus;

    /** 当前处理人 */
    private String currentHandler;

    /** 确认完成时间 */
    private Date confirmFinishTime;

    /** 送车人 */
    private String carDeliverer;

    /** 维修项目名称 */
    private String repairItemName;

    /** 结算方 */
    private String settlementParty;

    /** 品目编码 */
    private String itemCode;

    /** 品目名称 */
    private String itemName;

    /** 品目类型 */
    private String itemType;

    /** 品牌 */
    private String brand;

    /** 规格信息 */
    private String specInfo;

    /** 型号 */
    @TableField("\"model\"")
    private String model;

    /** 单位 */
    private String unit;

    /** 适用车型 */
    private String applicableVehicle;

    /** 来源 */
    private String source;

    /** 产地 */
    private String produceArea;

    /** 配件标识 */
    private String partMark;

    /** 数量 */
    private Double quantity;

    /** 数据导入时间 */
    private Date importTime;

    /** 文件id */
    private Long fileId;

}
