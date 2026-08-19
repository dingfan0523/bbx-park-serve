package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "日常管理分析")
public class ElectricityManageAnalysisModel {
    @ApiModelProperty(value = "抄表计划数量")
    private Integer meterReadingPlanCount;

    @ApiModelProperty(value = "执行中的抄表计划数量")
    private Integer runningMeterReadingPlanCount;

    @ApiModelProperty(value = "表数量")
    private Integer meterCount;

    @ApiModelProperty(value = "抄表次数")
    private Integer meterReadingCount;
}
