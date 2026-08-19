package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 15:16
 */
@Data
@ApiModel(value = "会议服务项目概览")
public class MeetingServiceOverview {
    @ApiModelProperty(value = "会议服务项目数量")
    private Integer serviceItemCount;
    @ApiModelProperty(value = "会议室数量")
    private Integer meetingRoomCount;
    @ApiModelProperty(value = "会议室占比(%)")
    private Double meetingRoomRatio;
    @ApiModelProperty(value = "会议服务提供次数")
    private Integer serviceProvideCount;
    @ApiModelProperty(value = "会议服务完成占比(%)")
    private Double serviceCompleteRatio;
}
