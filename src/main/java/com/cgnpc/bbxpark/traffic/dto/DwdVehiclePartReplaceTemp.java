package com.cgnpc.bbxpark.traffic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 车辆配件更换记录临时模型
 */
@Data
public class DwdVehiclePartReplaceTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /** 车牌号 */
    private String plate_num;

    /** 车辆单位 */
    private String vehicle_dept;

    /** 申请日期 */
    private String apply_date;

    /** 公里数(千米) */
    private String mileage;

    /** 车型 */
    private String vehicle_type;

    /** 更换数量(个) */
    private Object replace_count;

    /** 申报人 */
    private String reporter;

    /** 更换日期 */
    private String replace_date;

    /** 车管签名 */
    private String vehicle_admin_sign;

    /** 更换原因 */
    private String replace_reason;

    /** 送车人 */
    private String car_deliverer;

    /** 流程状态 */
    private String process_status;

    /** 当前处理人工号 */
    private String current_handler_id;

    /** 备注 */
    private String remark;

    /** 数据导入时间 */
    private String import_time;

}
