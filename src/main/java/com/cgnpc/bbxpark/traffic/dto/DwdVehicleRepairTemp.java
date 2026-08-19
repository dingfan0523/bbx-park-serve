package com.cgnpc.bbxpark.traffic.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆维修信息临时模型
 */
@Data
public class DwdVehicleRepairTemp implements Serializable {

    private String id;

    /** 车牌号 */
    private String plate_num;

    /** 车辆所属单位 */
    private String vehicle_dept;

    /** 作业单号 */
    private String work_order_no;

    /** 派工员 */
    private String dispatcher;

    /** 报修人 */
    private String reporter;

    /** 申报日期 */
    private String apply_date;

    /** 维修状态 */
    private String repair_status;

    /** 当前处理人 */
    private String current_handler;

    /** 确认完成时间 */
    private String confirm_finish_time;

    /** 送车人 */
    private String car_deliverer;

    /** 维修项目名称 */
    private String repair_item_name;

    /** 结算方 */
    private String settlement_party;

    /** 品目编码 */
    private String item_code;

    /** 品目名称 */
    private String item_name;

    /** 品目类型 */
    private String item_type;

    /** 品牌 */
    private String brand;

    /** 规格信息 */
    private String spec_info;

    /** 型号 */
    private String model;

    /** 单位 */
    private String unit;

    /** 适用车型 */
    private String applicable_vehicle;

    /** 来源 */
    private String source;

    /** 产地 */
    private String produce_area;

    /** 配件标识 */
    private String part_mark;

    /** 数量 */
    private String quantity;

    /** 数据导入时间 */
    private String import_time;
}
