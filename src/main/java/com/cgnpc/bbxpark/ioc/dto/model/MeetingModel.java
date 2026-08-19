package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 15:53
 */
@Data
@ApiModel(value = "会议")
public class MeetingModel {
    @ApiModelProperty(value = "会议ID")
    private String id;
    @ApiModelProperty(value = "会议名称")
    private String meetingName;
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "会议室地点")
    private String roomLocation;
    @ApiModelProperty(value = "实际开始时间")
    private Date actualStartTime;
    @ApiModelProperty(value = "实际结束时间")
    private Date actualEndTime;
}
