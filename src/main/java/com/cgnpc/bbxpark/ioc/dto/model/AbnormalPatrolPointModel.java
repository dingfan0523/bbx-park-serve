package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "异常巡更点")
public class AbnormalPatrolPointModel {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "巡更点")
    private String name;

    @ApiModelProperty(value = "巡更时间")
    private Date time;

    @ApiModelProperty(value = "巡更点类型")
    private Integer type;

    @ApiModelProperty(value = "巡更路线id")
    private Long routeId;

    @ApiModelProperty(value = "关联路线")
    private String routeName;

    @ApiModelProperty(value = "巡更点位置")
    private String spaceName;

    @ApiModelProperty(value = "异常说明")
    private String errorRemark;
}
