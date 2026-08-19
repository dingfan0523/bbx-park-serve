package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:15
 */
@Data
@ApiModel(value = "会议室使用排行")
public class MeetingRoomUse {
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "使用次数")
    private Integer useCount;
}
