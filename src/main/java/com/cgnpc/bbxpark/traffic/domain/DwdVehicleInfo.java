package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆信息表实体类
 * 对应数据库表: MATRIX.dwd_vehicle_info
 */
@Data
@TableName("dwd_vehicle_info")
public class DwdVehicleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（建议使用SYS_GUID()或应用生成）
     */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 车型划分
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
     */
    private String useStatus;

    /**
     * 车辆运行状态
     */
    private String runStatus;

    /**
     * 行驶里程
     */
    private Double mileage;

    /**
     * 下次年审日期
     */
    private Date nextInspectDate;

    /**
     * 数据导入时间
     */
    private Date importTime;

    /** 文件id */
    private Long fileId;
}
