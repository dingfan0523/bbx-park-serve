package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

/**
 * 驾驶员分析模型
 */
@Data
public class TrafficDriverCountModel {
    /**
     * 性别
     */
    private String gender;

    /**
     * 数量
     */
    private Integer count;

}