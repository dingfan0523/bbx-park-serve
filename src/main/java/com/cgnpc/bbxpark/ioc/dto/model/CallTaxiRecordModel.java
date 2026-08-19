package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "电召车出车记录")
public class CallTaxiRecordModel {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "乘车人")
    private String passenger;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "乘坐人数")
    private Integer passengerNum;

    @ApiModelProperty(value = "叫车原因")
    private String callReason;

    @ApiModelProperty(value = "起点")
    private String startPoint;

    @ApiModelProperty(value = "终点")
    private String endPoint;

    @ApiModelProperty(value = "审批结果")
    private String approveResult;
}
