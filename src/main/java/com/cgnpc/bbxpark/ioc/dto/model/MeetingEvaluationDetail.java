package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 15:17
 */
@Data
@ApiModel(value = "会议评价详情")
public class MeetingEvaluationDetail {
    @ApiModelProperty(value = "会议名称")
    private String meetingName;
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "会服人员工号")
    private String staffId;
    @ApiModelProperty(value = "会服人员名称")
    private String staffName;
    @ApiModelProperty(value = "评价分数")
    private Integer score;
    @ApiModelProperty(value = "评价内容")
    private String content;
    @ApiModelProperty(value = "评价时间")
    private Date evaluateTime;
}
