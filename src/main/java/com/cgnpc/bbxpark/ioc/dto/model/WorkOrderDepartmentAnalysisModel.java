package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏工单部门分析
 */
@Data
public class WorkOrderDepartmentAnalysisModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门id")
    private String id;

    @ApiModelProperty(value = "完成数量")
    private Integer completeNum;

    @ApiModelProperty(value = "未完成数量")
    private Integer noCompleteNum;

    @ApiModelProperty(value = "总数量")
    private Integer totalNum;
}
