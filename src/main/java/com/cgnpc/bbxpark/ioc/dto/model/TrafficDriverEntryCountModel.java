package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

/**
 * 驾驶员入职人数统计模型
 */
@Data
public class TrafficDriverEntryCountModel {
    /**
     * 日期（如：2025-01）
     */
    private String date;

    /**
     * 入职人数
     */
    private Integer count;
}