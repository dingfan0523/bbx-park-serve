package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏工单评价不满意分析
 */
@Data
public class WorkOrderScreenUnsatisfiedTraceModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总数量")
    private Integer totalNum;

    @ApiModelProperty(value = "类型集合")
    private List<WorkOrderSourceModel> sourceAnalysisModels;
}
