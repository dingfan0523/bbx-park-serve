package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "部门设备分布")
public class DeviceDeptDistributionModel {

    @ApiModelProperty(value = "部门ID")
    private String deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceCount;

    @ApiModelProperty(value = "数量比例%")
    private Double numRate;
}
