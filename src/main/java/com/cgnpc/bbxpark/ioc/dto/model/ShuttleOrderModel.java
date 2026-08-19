package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "便民班车订单")
public class ShuttleOrderModel {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "运行组织机构")
    private String runOrg;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "线路方向")
    private String lineDirection;

    @ApiModelProperty(value = "乘坐人数")
    private Integer rideNum;

    @ApiModelProperty(value = "上车站点")
    private String startStation;

    @ApiModelProperty(value = "下车站点")
    private String endStation;
}
