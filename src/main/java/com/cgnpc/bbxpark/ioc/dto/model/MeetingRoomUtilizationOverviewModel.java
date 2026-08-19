package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "会议室利用率概览模型")
public class MeetingRoomUtilizationOverviewModel {
    @ApiModelProperty(value = "高负荷会议室名称")
    private String highLoadMeetingRoom;
    @ApiModelProperty(value = "低负荷会议室名称")
    private String lowLoadMeetingRoom;
    @ApiModelProperty(value = "高负荷会议室平均使用率")
    private Double avgUseRate;
}