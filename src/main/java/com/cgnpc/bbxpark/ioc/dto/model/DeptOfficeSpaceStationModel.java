package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "部门办公空间分析-工位维度模型")
public class DeptOfficeSpaceStationModel {
    @ApiModelProperty(value = "部门id")
    private String deptId;
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    @ApiModelProperty(value = "已分配工位数量")
    private Integer allocatedStations;
    @ApiModelProperty(value = "空闲工位数量")
    private Integer freeStations;
    @ApiModelProperty(value = "工位利用率")
    private Double utilizationRate;
}