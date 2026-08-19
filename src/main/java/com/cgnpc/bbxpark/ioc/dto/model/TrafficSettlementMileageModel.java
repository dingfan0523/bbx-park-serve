package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

/**
 * 车辆费用里程信息模型
 */
@Data
public class TrafficSettlementMileageModel {
    /**
     * 车牌号
     */
    private String carPlate;
    /**
     * 当前行驶里程（公里）
     */
    private Double currentMileage;
}