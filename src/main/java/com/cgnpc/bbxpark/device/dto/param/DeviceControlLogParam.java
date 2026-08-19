
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class DeviceControlLogParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4680374942936154317L;

    @ApiModelProperty(value = "设备id.")
    private String deviceId;
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;
    @ApiModelProperty(value = "操作人姓名.")
    private String name;
    @ApiModelProperty(value = "操作人工号.")
    private String staffid;
    @ApiModelProperty(value = "渠道.")
    private String channel;
    @ApiModelProperty(value = "结果.")
    private String result;
}
