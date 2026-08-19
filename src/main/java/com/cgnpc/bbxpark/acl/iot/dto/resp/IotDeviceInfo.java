package com.cgnpc.bbxpark.acl.iot.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class IotDeviceInfo {
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "设备dn")
    private String deviceDn;
    @ApiModelProperty(value = "创建时间")
    private Long createAt;
    @ApiModelProperty(value = "设备在线时间")
    private Long onlineTime;
    @ApiModelProperty(value = "设备离线时间")
    private Long offlineTime;
    @ApiModelProperty(value = "父级id")
    private String parentId;
    @ApiModelProperty(value = "产品key")
    private String productKey;
    @ApiModelProperty(value = "设备状态")
    private Boolean online;
    @ApiModelProperty(value = "所属分组")
    private String groupId;
    /**
     * 设备属性
     */
    @ApiModelProperty(value = "设备属性")
    private Map<String, Object> propertys = new HashMap<>();
}
