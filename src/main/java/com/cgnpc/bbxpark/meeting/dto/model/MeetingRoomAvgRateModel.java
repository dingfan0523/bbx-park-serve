package com.cgnpc.bbxpark.meeting.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "会议室平均使用率模型")
public class MeetingRoomAvgRateModel {
    @ApiModelProperty(value = "会议室id.")
    private Long id;
    @ApiModelProperty(value = "平均使用率.")
    private Double avgRate;

}
