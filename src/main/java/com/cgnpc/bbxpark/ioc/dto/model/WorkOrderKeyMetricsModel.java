package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏工单核心指标
 */
@Data
public class WorkOrderKeyMetricsModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "进行中数量")
    private Integer goingCount;

    @ApiModelProperty(value = "平均处理时长（h）")
    private Double avgHandleTime;

    @ApiModelProperty(value = "一次解决率%")
    private Double oneTimeRate;

    @ApiModelProperty(value = "首次响应时间（h）")
    private Double firstHandleTime;
}
