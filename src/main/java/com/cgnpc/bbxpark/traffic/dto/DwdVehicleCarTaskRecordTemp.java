package com.cgnpc.bbxpark.traffic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 车辆行驶记录临时模型
 */
@Data
public class DwdVehicleCarTaskRecordTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 车辆单位 */
    private String car_company;

    /** 车牌号 */
    private String car_plate;

    /** 驾驶员 */
    private String driver_name;

    /** 任务类型 */
    private String task_type;

    /** 任务情况 */
    private String task_detail;

    /** 出车时间 */
    private String depart_time;

    /** 出车前公里数（KM） */
    private String before_mileage;

    /** 收车时间 */
    private String return_time;

    /** 收车后公里数（KM） */
    private String after_mileage;

    /** 单趟行驶里程 */
    private String single_mileage;

    /** 用车单位 */
    private String use_dept;

    /** 用车人 */
    private String car_user;

    /** 数据导入时间 */
    private String import_time;
}
