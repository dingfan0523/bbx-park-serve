package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "异常巡检点")
public class AbnormalInspectionPointModel {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "巡检点")
    private String name;

    @ApiModelProperty(value = "巡检点位置")
    private String spaceName;

    @ApiModelProperty(value = "异常说明")
    private String errorRemark;

    @ApiModelProperty(value = "关联工单id")
    private Long workOrderId;

    @ApiModelProperty(value = "关联工单编码")
    private String workOrderCode;

    @ApiModelProperty(value = "巡检时间")
    private Date time;
}
