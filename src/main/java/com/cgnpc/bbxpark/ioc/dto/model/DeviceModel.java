package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "设备模型")
public class DeviceModel {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;
    @ApiModelProperty(value = "设备分组id")
    private Long groupId;
    @ApiModelProperty(value = "设备分组编码")
    private String groupCode;
    @ApiModelProperty(value = "设备分组名称")
    private String groupName;
    @ApiModelProperty(value = "支路名称")
    private String branchName;
    @ApiModelProperty(value = "设备图片")
    private String image;
}
