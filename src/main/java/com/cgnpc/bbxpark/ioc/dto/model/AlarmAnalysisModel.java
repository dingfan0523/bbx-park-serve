package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "告警分析分组")
public class AlarmAnalysisModel {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "类型:group->设备分组;device->设备")
    private String type;
    @ApiModelProperty(value = "告警数量")
    private Integer alarmCount;
}
