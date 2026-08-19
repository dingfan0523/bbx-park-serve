package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "设备资产总览模型")
@Data
public class DeviceOverviewModel {
    @ApiModelProperty(value = "设备总数")
    private Integer total;
    @ApiModelProperty(value = "智能化设备数量")
    private Integer intelligentCount;
    @ApiModelProperty(value = "非智能化设备数量")
    private Integer nonIntelligentCount;
}
