package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 驾驶员工作量分析模型
 */
@Data
public class TrafficDriverWorkloadModel {

    /**
     * 驾驶员姓名
     */
    private String driverName;


    /**
     * 总行驶里程（公里）
     */
    private Double totalMileage;
}