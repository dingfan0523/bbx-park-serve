package com.cgnpc.bbxpark.traffic.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆保养信息临时表
 */
@Data
public class DwdVehicleMaintainTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /** 车牌号 */
    private String plate_num;

    /** 车辆单位 */
    private String vehicle_dept;

    /** 车辆类型 */
    private String vehicle_type;

    /** 上次保养日期 */
    private String last_maintain_date;

    /** 保养日期 */
    private String maintain_date;

    /** 进厂里程数 */
    private String in_mileage;

    /** 申报单位 */
    private String apply_dept;

    /** 申报日期 */
    private String apply_date;

    /** 保养地点 */
    private String maintain_location;

    /** 预估费用 */
    private String estimate_cost;

    /** 发起人 */
    private String sponsor;

    /** 保养状态 */
    private String maintain_status;

    /** 送车人 */
    private String car_deliverer;

    /** 保养项目名称 */
    private String maintain_item_name;

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

    /** 数量 */
    private String quantity;

    /** 单价 */
    private String price;

    /** 品目金额 */
    private String item_amount;

    /** 数据导入时间 */
    private String import_time;

}
