package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "楼层设备数量")
public class FloorDeviceCountModel {
    @ApiModelProperty(value = "空间名称")
    private String spaceName;
    @ApiModelProperty(value = "空间面积")
    private Double spaceArea;
    @ApiModelProperty(value = "智能化设备数量")
    private Integer intelligentDeviceCount;
    @ApiModelProperty(value = "在线数量")
    private Integer onlineCount;
    @ApiModelProperty(value = "离线数量")
    private Integer offlineCount;
}
