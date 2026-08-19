package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 移动端-简单的预约模型
 * @author dingfan
 * @date 2024/8/15 9:45
 */
@Data
public class AppSimpleReserveModel {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "会议主题.")
    private String reserveName;
    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;
    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;
    @ApiModelProperty(value = "签到标识")
    private Boolean signFlag = false;
}
