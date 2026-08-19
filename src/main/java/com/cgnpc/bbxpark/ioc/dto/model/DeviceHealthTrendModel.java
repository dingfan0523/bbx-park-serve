package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "设备健康趋势")
public class DeviceHealthTrendModel {

    @ApiModelProperty(value = "日期(MM-dd)")
    private String date;
    @ApiModelProperty(value = "在线率(%)")
    private Double onlineRate;
    @ApiModelProperty(value = "告警率(%)")
    private Double alarmRate;
}
