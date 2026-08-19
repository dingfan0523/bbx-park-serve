package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:21
 */
@Data
@Builder
@ApiModel(value = "每日会议室健康度")
public class DailyRoomHealth {
    @ApiModelProperty(value = "星期几(如: 周一)")
    private String weekday;
    @ApiModelProperty(value = "会议数量")
    private Integer meetingCount;
    @ApiModelProperty(value = "自检次数")
    private Integer selfCheckCount;
    @ApiModelProperty(value = "工单数量")
    private Integer workOrderCount;
    @ApiModelProperty(value = "告警次数")
    private Integer alarmCount;
    @ApiModelProperty(value = "设备数量")
    private Integer deviceCount;
    @ApiModelProperty(value = "服务次数")
    private Integer serviceCount;
}
