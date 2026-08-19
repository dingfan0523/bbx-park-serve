package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电召车出车记录实体
 */
@Data
@TableName("dwd_vehicle_apply")
public class VehicleApply implements Serializable {

    /** 主键ID */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;
    /** 乘车人 */
    private String passenger;
    /** 部门 */
    private String department;
    /** 乘坐人数 */
    private Integer passengerNum;
    /** 联系方式 */
    private String contact;
    /** 起始点 */
    private String startPoint;
    /** 终点 */
    private String endPoint;
    /** 叫车原因 */
    private String callReason;
    /** 电召电话 */
    private String dispatchTel;
    /** 驾驶员（常规运输） */
    private String driverRegular;
    /** 电召手机 */
    private String dispatchMobile;
    /** 车牌号（常规） */
    private String plateRegular;
    /** 手机号 */
    private String mobile;
    /** 乘车人 */
    private String passenger2;
    /** 部门 */
    private String department2;
    /** 联系方式 */
    private String contact2;
    /** 用车类型 */
    private String carType;
    /** 宿舍点 */
    private String dormitory;
    /** 厂区位置 */
    private String factoryLocation;
    /** 用车时间（开元/天丰园） */
    private Date useTime1;
    /** 用车时间（半山半岛/天和/南宋） */
    private Date useTime2;
    /** 驾驶员 */
    private String driver;
    /** 驾驶员联系方式 */
    private String driverContact;
    /** 车牌号 */
    private String plateNum;
    /** 运货皮卡1 */
    private String truckPickup1;
    /** 运货皮卡2 */
    private String truckPickup2;
    /** 车辆忙闲1 */
    private String vehicleStatus1;
    /** 车辆忙闲2 */
    private String vehicleStatus2;
    /** 车辆归属 */
    private String vehicleOwner;
    /** 驾驶员（工业运输） */
    private String driverIndustrial;
    /** 驾驶员联系方式 */
    private String driverContactIndustrial;
    /** 车牌号（运货） */
    private String plateTruck;
    /** 车辆忙闲 */
    private String vehicleStatus;
    /** 提交人 */
    private String submitter;
    /** 是否为CNOC员工 */
    private String isCnocEmployee;
    /** 提交人组织 */
    private String submitterOrg;
    /** 是否为CNOC员工（夜间） */
    private String isCnocEmployeeNight;
    /** 创建时间 */
    private Date createTime;
    /** 驾驶员2 */
    private String driver2;
    /** 修改时间 */
    private Date updateTime;
    /** 当前审批节点 */
    private String approveNode;
    /** 是否应急 */
    private String isEmergency;
    /** 实例状态 */
    private String instanceStatus;
    /** 起始点 */
    private String startPoint2;
    /** 审批结果 */
    private String approveResult;
    /** 终点 */
    private String endPoint2;
    /** 驾驶员5 */
    private String driver5;
    /** 拒绝原因说明 */
    private String rejectReason;
    /** 是否派车 */
    private String isAssign;
    /** 驾驶员3 */
    private String driver3;
    /** 数据导入时间 */
    private Date importTime;
    /** 文件id */
    private Long fileId;
}
