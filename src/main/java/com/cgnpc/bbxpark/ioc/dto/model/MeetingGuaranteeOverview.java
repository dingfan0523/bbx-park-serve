package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 15:14
 */
@Data
@Builder
@ApiModel(value = "会议保障专项概览")
public class MeetingGuaranteeOverview {
    @ApiModelProperty(value = "会前检查项数量")
    private Integer preCheckItemCount;
    @ApiModelProperty(value = "会议室数量")
    private Integer meetingRoomCount;
    @ApiModelProperty(value = "会议室数量占比(%)")
    private Double meetingRoomRatio;
    @ApiModelProperty(value = "会前检查项执行次数")
    private Integer preCheckExecuteCount;
    @ApiModelProperty(value = "执行完成占比(%)")
    private Double executeCompleteRatio;
}
