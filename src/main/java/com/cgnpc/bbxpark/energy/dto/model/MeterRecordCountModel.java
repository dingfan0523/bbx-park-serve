package com.cgnpc.bbxpark.energy.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/***
 * @Description 抄表记录能耗概览模型
 * @author huangyongtao
 * @date 2025/4/22 13:53
 */
@Data
public class MeterRecordCountModel {

    @ApiModelProperty(value = "最新时间")
    private Date newTime;

    @ApiModelProperty(value = "本周用量")
    private Double weekValue = 0d;

    @ApiModelProperty(value = "上周用量")
    private Double lastWeekValue = 0d;

    @ApiModelProperty(value = "上周用量差")
    private Double lastWeekDifferValue = 0d;

    @ApiModelProperty(value = "去年本周用量")
    private Double lastYearWeekValue = 0d;

    @ApiModelProperty(value = "去年本周用量差")
    private Double lastYearWeekDifferValue = 0d;

    @ApiModelProperty(value = "本月用量")
    private Double monthValue = 0d;

    @ApiModelProperty(value = "上月用量")
    private Double lastMonthValue = 0d;

    @ApiModelProperty(value = "上月用量差")
    private Double lastMonthDifferValue = 0d;

    @ApiModelProperty(value = "去年本月用量")
    private Double lastYearMonthValue = 0d;

    @ApiModelProperty(value = "去年本月用量差")
    private Double lastYearMonthDifferValue = 0d;

    @ApiModelProperty(value = "本年用量")
    private Double yearValue = 0d;

    @ApiModelProperty(value = "去年用量")
    private Double lastYearValue = 0d;

    @ApiModelProperty(value = "去年用量差")
    private Double lastYearDifferValue = 0d;
}
