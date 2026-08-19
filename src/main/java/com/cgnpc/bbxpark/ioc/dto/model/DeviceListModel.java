package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "设备列表模型")
public class DeviceListModel {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    private Integer deviceLevel = 30;
    @ApiModelProperty(value = "设备分类；1：弱电设备；2：CIT设备；3：固定资产.")
    private Integer deviceCategory;
    @ApiModelProperty(value = "使用部门")
    private String departmentName;
    @ApiModelProperty(value = "物联设备平台;（0：非物联网设备；1：自有平台；2：统建平台；3：安消平台）.")
    private Integer iotDevicePlatform = 0;
    @ApiModelProperty(value = "启用状态;（0->否;1->是）.")
    private Integer enableStatus = 1;
}