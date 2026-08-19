package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "电召车使用部门统计")
public class CallTaxiDeptStatModel {
    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "次数")
    private Integer count;
}
