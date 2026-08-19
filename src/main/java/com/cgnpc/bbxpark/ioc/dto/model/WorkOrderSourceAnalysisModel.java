package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏工单来源分析
 */
@Data
public class WorkOrderSourceAnalysisModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "来源名称")
    private String name;

    @ApiModelProperty(value = "数量")
    private Integer totalNum;

    @ApiModelProperty(value = "数量比例%")
    private Double numRate;
}
