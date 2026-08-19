package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "实时组态(用水)")
public class WaterRealtimeConfigModel {
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "抄表编码")
    private String readingCode;

    @ApiModelProperty(value = "设备位置")
    private String spaceName;

    @ApiModelProperty(value = "抄表值")
    private Double readingValue;

    @ApiModelProperty(value = "是否正常")
    private Boolean normal;

    @ApiModelProperty(value = "下级节点")
    private List<WaterRealtimeConfigModel> children;
}
