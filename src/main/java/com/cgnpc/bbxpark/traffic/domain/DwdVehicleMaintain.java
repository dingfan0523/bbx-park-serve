package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆保养信息表实体类
 * 对应数据库表: MATRIX.dwd_vehicle_maintain
 */
@Data
@TableName("dwd_vehicle_maintain")
public class DwdVehicleMaintain implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID（建议使用SYS_GUID()或应用生成） */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /** 车牌号 */
    private String plateNum;

    /** 车辆单位 */
    private String vehicleDept;

    /** 车辆类型 */
    private String vehicleType;

    /** 上次保养日期 */
    private Date lastMaintainDate;

    /** 保养日期 */
    private Date maintainDate;

    /** 进厂里程数 */
    private Double inMileage;

    /** 申报单位 */
    private String applyDept;

    /** 申报日期 */
    private Date applyDate;

    /** 保养地点 */
    private String maintainLocation;

    /** 预估费用 */
    private Double estimateCost;

    /** 发起人 */
    private String sponsor;

    /** 保养状态 */
    private String maintainStatus;

    /** 送车人 */
    private String carDeliverer;

    /** 保养项目名称 */
    private String maintainItemName;

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

    /** 数量 */
    private Double quantity;

    /** 单价 */
    private Double price;

    /** 品目金额 */
    private Double itemAmount;

    /** 数据导入时间 */
    private Date importTime;

    /** 文件id */
    private Long fileId;

}
