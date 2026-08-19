package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "租车记录")
public class CarRentRecordModel {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "租车类型")
    private String rentType;

    @ApiModelProperty(value = "租车时间")
    private String rentTime;

    @ApiModelProperty(value = "租车开始时间")
    private Date rentStartTime;

    @ApiModelProperty(value = "租车结束时间")
    private Date rentEndTime;

    @ApiModelProperty(value = "租车天数")
    private Double rentDays;

    @ApiModelProperty(value = "具体明细")
    private String detailInfo;

    @ApiModelProperty(value = "审批结果")
    private String approveResult;

    @ApiModelProperty(value = "状态")
    private String instanceStatus;
}
