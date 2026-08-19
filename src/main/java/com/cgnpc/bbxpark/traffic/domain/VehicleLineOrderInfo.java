package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 便民班车/线路乘车订单表实体
 */
@Data
@TableName("dwd_vehicle_line_order_info")
public class VehicleLineOrderInfo implements Serializable {

    /** 主键ID */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;
    /** 运行组织机构 */
    private String runOrg;
    /** 线路分组 */
    private String lineGroup;
    /** 线路名称 */
    private String lineName;
    /** 线路方向 */
    private String lineDirection;
    /** 任务日期 */
    private Date taskDate;
    /** 发车时刻 */
    private String departTime;
    /** 预定人数 */
    private Integer reserveNum;
    /** 未支付人数 */
    private Integer unpaidNum;
    /** 已支付人数 */
    private Integer paidNum;
    /** 金额合计 */
    private BigDecimal totalAmount;
    /** 订车人 */
    private String orderUser;
    /** 订车人单位 */
    private String orderUserDept;
    /** 预留电话 */
    private String reservePhone;
    /** 上车站点 */
    private String startStation;
    /** 下车站点 */
    private String endStation;
    /** 乘坐人数 */
    private Integer rideNum;
    /** 儿童人数 */
    private Integer childNum;
    /** 订单金额 */
    private BigDecimal orderAmount;
    /** 订单状态 */
    private String orderStatus;
    /** 支付方式 */
    private String payType;
    /** 实际支付时间 */
    private Date actualPayTime;
    /** 备注 */
    private String remark;
    /** 航班/列车信息 */
    private String flightTrainInfo;
    /** 数据导入时间 */
    private Date importTime;
    /** 文件id */
    private Long fileId;
}
