package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 租车记录表实体类
 * 对应数据库表: MATRIX.dwd_vehicle_car_rent_apply
 */
@Data
@TableName("dwd_vehicle_car_rent_apply")
public class DwdVehicleCarRentApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /** 实例标题 */
    private String instanceTitle;

    /** 申请人 */
    private String applicant;

    /** 用车部门 */
    private String useDept;

    /** 申请日期 */
    private Date applyDate;

    /** 租车类型 */
    private String rentType;

    /** 租车时间"2024-07-15_2024-07-19*/
    private String rentTime;

    /** 租车开始时间 */
    private Date rentStartTime;

    /** 租车结束时间 */
    private Date rentEndTime;

    /** 租车天数 */
    private Double rentDays;

    /** 具体明细 */
    private String detailInfo;

    /** 附件 */
    private String attachment;

    /** 用车人 */
    private String carUser;

    /** 租车事由 */
    private String rentReason;

    /** 提交人 */
    private String submitter;

    /** 提交人组织 */
    private String submitterOrg;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

    /** 当前审批节点 */
    private String currentApproveNode;

    /** 实例状态 */
    private String instanceStatus;

    /** 审批结果 */
    private String approveResult;

    /** 数据导入时间 */
    private Date importTime;

    /** 文件id */
    private Long fileId;

}
