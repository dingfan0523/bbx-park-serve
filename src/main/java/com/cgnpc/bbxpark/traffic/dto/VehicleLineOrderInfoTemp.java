package com.cgnpc.bbxpark.traffic.dto;

import lombok.Data;
/**
 * 车辆维修信息导入模板
 *
 */
@Data
public class VehicleLineOrderInfoTemp {
    private String id;
    /** 运行组织机构 */
    private String run_org;
    /** 线路分组 */
    private String line_group;
    /** 线路名称 */
    private String line_name;
    /** 线路方向 */
    private String line_direction;
    /** 任务日期 */
    private String task_date;
    /** 发车时刻 */
    private String depart_time;
    /** 预定人数 */
    private String reserve_num;
    /** 未支付人数 */
    private String unpaid_num;
    /** 已支付人数 */
    private String paid_num;
    /** 金额合计 */
    private String total_amount;
    /** 订车人 */
    private String order_user;
    /** 订车人单位 */
    private String order_user_dept;
    /** 预留电话 */
    private String reserve_phone;
    /** 上车站点 */
    private String start_station;
    /** 下车站点 */
    private String end_station;
    /** 乘坐人数 */
    private String ride_num;
    /** 儿童人数 */
    private String child_num;
    /** 订单金额 */
    private String order_amount;
    /** 订单状态 */
    private String order_status;
    /** 支付方式 */
    private String pay_type;
    /** 实际支付时间 */
    private String actual_pay_time;
    /** 备注 */
    private String remark;
    /** 航班/列车信息 */
    private String flight_train_info;
    /** 数据导入时间 */
    private String import_time;

}
