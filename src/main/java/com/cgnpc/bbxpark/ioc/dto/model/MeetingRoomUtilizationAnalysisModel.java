package com.cgnpc.bbxpark.ioc.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "会议室利用率分析(30天)模型")
public class MeetingRoomUtilizationAnalysisModel {
    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    @ApiModelProperty(value = "利用率")
    private Double useRate;
}