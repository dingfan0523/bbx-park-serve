package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "巡检故障排行项")
public class InspectionFaultRankModel {
    @ApiModelProperty(value = "巡检点id")
    private Long id;

    @ApiModelProperty(value = "巡检点名称")
    private String name;

    @ApiModelProperty(value = "故障次数")
    private Integer count;

    @ApiModelProperty(value = "空间名称")
    private String spaceName;
}
