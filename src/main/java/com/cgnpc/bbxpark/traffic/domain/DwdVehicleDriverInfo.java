package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 司机表实体类
 * 对应数据库表: MATRIX.dwd_vehicle_driver_info
 */
@Data
@TableName("dwd_vehicle_driver_info")
public class DwdVehicleDriverInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /** 驾驶员 */
    private String driverName;

    /** 驾驶员班组 */
    private String driverTeam;

    /** 电话 */
    private String phone;

    /** 性别 */
    private String gender;

    /** 年龄 */
    private Integer age;

    /** 工作时间 */
    private Date workHours;

    /** 入职时间 */
    private Date entryTime;

    /** 广核工龄 */
    private Double gnhWorkYears;

    /** 国家工龄 */
    private Double nationWorkYears;

    /** 工作状态 */
    private String workStatus;

    /** 驾驶证号 */
    private String driverLicenseNo;

    /** 驾驶证有效结束日期 */
    private Date licenseExpireTime;

    /** 数据导入时间 */
    private Date importTime;

    /** 文件id */
    private Long fileId;

}
