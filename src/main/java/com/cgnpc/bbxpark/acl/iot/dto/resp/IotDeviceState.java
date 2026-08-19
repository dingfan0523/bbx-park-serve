package com.cgnpc.bbxpark.acl.iot.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class IotDeviceState implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id")
    private String deviceId;
    @ApiModelProperty(value = "产品key")
    private String productKey;
    @ApiModelProperty(value = "是否在线")
    private Boolean online;
}
