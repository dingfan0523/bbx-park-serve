package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:19
 */
@Data
@ApiModel(value = "试点会议室概览")
public class PilotMeetingOverview {
    @ApiModelProperty(value = "试点会议室数量")
    private Integer pilotRoomCount;
    @ApiModelProperty(value = "总会议室数量")
    private Integer totalRoomCount;
    @ApiModelProperty(value = "总会议数量")
    private Integer totalMeetingCount;
    @ApiModelProperty(value = "服务总次数")
    private Integer totalServiceCount;
    @ApiModelProperty(value = "平均会服评价分数")
    private Double avgServiceScore;
}
