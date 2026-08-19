package com.cgnpc.bbxpark.traffic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 租车记录临时模型
 */
@Data
public class DwdVehicleCarRentApplyTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 实例标题 */
    private String instance_title;

    /** 申请人 */
    private String applicant;

    /** 用车部门 */
    private String use_dept;

    /** 申请日期 */
    private String apply_date;

    /** 租车类型 */
    private String rent_type;

    /** 租车时间"2024-07-15_2024-07-19*/
    private String rent_time;

    /** 租车天数 */
    private String rent_days;

    /** 具体明细 */
    private String detail_info;

    /** 附件 */
    private String attachment;

    /** 用车人 */
    private String car_user;

    /** 租车事由 */
    private String rent_reason;

    /** 提交人 */
    private String submitter;

    /** 提交人组织 */
    private String submitter_org;

    /** 创建时间 */
    private String create_time;

    /** 修改时间 */
    private String update_time;

    /** 当前审批节点 */
    private String current_approve_node;

    /** 实例状态 */
    private String instance_status;

    /** 审批结果 */
    private String approve_result;

    /** 数据导入时间 */
    private String import_time;

}
