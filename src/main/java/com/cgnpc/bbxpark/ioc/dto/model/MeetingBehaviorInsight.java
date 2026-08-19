package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:16
 */
@Data
@ApiModel(value = "会议行为洞察")
public class MeetingBehaviorInsight {

    @ApiModelProperty(value = "警戒线数量阈值")
    private Integer warningThreshold;
    @ApiModelProperty(value = "会议时长分布")
    private List<DurationRange> ranges;

    @Data
    @ApiModel(value = "时长范围统计")
    public static class DurationRange {
        @ApiModelProperty(value = "时长范围，如'<30min'、'30-60min'")
        private String range;
        @ApiModelProperty(value = "会议数量")
        private Integer count;
    }
}
