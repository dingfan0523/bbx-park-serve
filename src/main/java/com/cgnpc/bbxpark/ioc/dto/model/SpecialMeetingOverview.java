package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:13
 */
@Data
@ApiModel(value = "专项会议室概览")
public class SpecialMeetingOverview {
    @ApiModelProperty(value = "专项会议室数量")
    private Integer specialRoomCount;
    @ApiModelProperty(value = "总会议室数量")
    private Integer totalRoomCount;
    @ApiModelProperty(value = "总会议数量")
    private Integer totalMeetingCount;
    @ApiModelProperty(value = "平均会议时长(小时)")
    private Double avgMeetingDuration;
    @ApiModelProperty(value = "本地会议数量")
    private Integer localMeetingCount;
    @ApiModelProperty(value = "视频会议数量")
    private Integer videoMeetingCount;
}
