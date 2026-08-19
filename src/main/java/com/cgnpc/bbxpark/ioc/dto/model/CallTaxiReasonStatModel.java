package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "电召车使用原因统计")
public class CallTaxiReasonStatModel {
    @ApiModelProperty(value = "叫车原因")
    private String callReason;

    @ApiModelProperty(value = "次数")
    private Integer count;
}
