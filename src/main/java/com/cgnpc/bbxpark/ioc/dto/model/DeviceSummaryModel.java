package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "设备详情模型")
public class DeviceSummaryModel {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;
    @ApiModelProperty(value = "设备位置")
    private String spaceName;
    @ApiModelProperty(value = "设备分组编码")
    private String groupCode;
    @ApiModelProperty(value = "设备分组名称")
    private String groupName;
    @ApiModelProperty(value = "设备在线状态(0->离线;1->在线)")
    private Integer deviceStatus = 1;
    @ApiModelProperty(value = "设备告警状态(0->否;1->是)")
    private Integer alarmStatus = 0;
    @ApiModelProperty(value = "设备dn")
    private String deviceDn;
    @ApiModelProperty(value = "设备ip")
    private String deviceIp;
    @ApiModelProperty(value = "设备属性")
    private List<DeviceProperty> deviceProperties;
}
