package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.util.List;

/**
 * 驾驶员分析模型
 */
@Data
public class TrafficDriverAnalysisModel {
    /**
     * 驾驶员总人数
     */
    private Integer totalDrivers;

    /**
     * 男性人数
     */
    private Integer maleCount;

    /**
     * 女性人数
     */
    private Integer femaleCount;

    /**
     * 男性比例（%）
     */
    private Double malePercentage;

    /**
     * 女性比例（%）
     */
    private Double femalePercentage;

}