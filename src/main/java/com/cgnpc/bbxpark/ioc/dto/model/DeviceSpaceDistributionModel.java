package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "空间设备分布")
public class DeviceSpaceDistributionModel {

    @ApiModelProperty(value = "空间ID")
    private String spaceId;

    @ApiModelProperty(value = "空间模型编码")
    private String sslcCode;

    @ApiModelProperty(value = "空间名称")
    private String spaceName;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceCount;

    @ApiModelProperty(value = "是否有下级空间")
    private Boolean hasChildren;
}
