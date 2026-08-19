package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆轮胎更换记录分页模型
 */
@Data
public class TrafficPartReplacePageModel {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 车牌号
     */
    private String plateNum;

    /**
     * 更换数量（个）
     */
    private Integer replaceCount;

    /**
     * 更换原因
     */
    private String replaceReason;

    /**
     * 更换日期
     */
    private Date replaceDate;

    /**
     * 公里数（千米）
     */
    private Double mileage;

    /**
     * 车型
     */
    private String vehicleType;

    /**
     * 申报人
     */
    private String reporter;

    /**
     * 流程状态
     */
    private String processStatus;
}