package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏工单来源分析
 */
@Data
public class WorkOrderSourceModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "来源名称")
    private String name;

    @ApiModelProperty(value = "来源类型")
    private String source;

    @ApiModelProperty(value = "数量")
    private Integer totalNum;

    @ApiModelProperty(value = "部门集合")
    private List<WorkOrderDepartmentAnalysisModel> departmentAnalysisModels;

}
