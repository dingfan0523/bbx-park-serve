package com.cgnpc.bbxpark.traffic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 车辆信息临时表
 */
@Data
public class DwdVehicleInfoTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * 车牌号
     */
    private String license_plate;

    /**
     * 车型划分
     */
    private String vehicle_type;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 车辆型号
     */
    private String vehicle_model;

    /**
     * 车辆单位
     */
    private String department;

    /**
     * 车辆使用状态
     */
    private String use_status;

    /**
     * 车辆运行状态
     */
    private String run_status;

    /**
     * 行驶里程
     */
    private String mileage;

    /**
     * 下次年审日期
     */
    private String next_inspect_date;

    /**
     * 数据导入时间
     */
    private String import_time;
}
