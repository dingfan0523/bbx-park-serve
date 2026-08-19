package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "巡更列表项")
public class PatrolRouteListItemModel {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "路线名称")
    private String name;

    @ApiModelProperty(value = "巡更点异常次数")
    private Integer pointAbnormalCount = 0;

    @ApiModelProperty(value = "巡更点总次数")
    private Integer pointTotalCount = 0;

    @ApiModelProperty(value = "建议")
    private String suggestion;
}
