package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "部门办公空间分析-面积维度模型")
public class DeptOfficeSpaceAreaModel {
    @ApiModelProperty(value = "部门id")
    private String deptId;
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    @ApiModelProperty(value = "办公面积")
    private Double officeArea;
    @ApiModelProperty(value = "人均办公面积")
    private Double avgOfficeArea;
}