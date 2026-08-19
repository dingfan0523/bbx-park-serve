package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "日常管理分析(用水)")
public class WaterManageAnalysisModel {
    @ApiModelProperty(value = "抄表计划数量")
    private Integer meterReadingPlanCount;

    @ApiModelProperty(value = "执行中的抄表计划数量")
    private Integer runningMeterReadingPlanCount;

    @ApiModelProperty(value = "水表数量")
    private Integer meterCount;

    @ApiModelProperty(value = "抄表次数")
    private Integer meterReadingCount;
}
