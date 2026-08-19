package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 15:08
 */
@Data
@ApiModel(value = "节能概览")
public class EnergySavingOverview {
    @ApiModelProperty(value = "节能试点会议室数量")
    private Integer pilotRoomCount;
    @ApiModelProperty(value = "总会议室数量")
    private Integer totalRoomCount;
    @ApiModelProperty(value = "节能会议室占比(%)")
    private Double pilotRoomRatio;
    @ApiModelProperty(value = "节能时长(小时)")
    private Double energySavingHours;
    @ApiModelProperty(value = "节约能耗(KWH)")
    private Double energySavedKwh;
    @ApiModelProperty(value = "节能方案执行次数")
    private Integer executionCount;
}
