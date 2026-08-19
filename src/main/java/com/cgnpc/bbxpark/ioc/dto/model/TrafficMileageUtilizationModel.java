package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 里程利用率趋势分析模型
 */
@Data
public class TrafficMileageUtilizationModel {
    /**
     * 月份（如：1月）
     */
    private String month;

    /**
     * 总包月公里数
     */
    private Double totalPackageMileage = 0d;

    /**
     * 月行驶公里数
     */
    private Double monthlyMileage = 0d;

    /**
     * 剩余公里数（包月公里数 - 月行驶公里数）
     */
    private Double remainingMileage = 0d;

}