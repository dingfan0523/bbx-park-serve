package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "总览数据")
@Data
public class OverviewData {
    @ApiModelProperty(value = "字段key")
    private String key;
    @ApiModelProperty(value = "字段值")
    private String value;
}
