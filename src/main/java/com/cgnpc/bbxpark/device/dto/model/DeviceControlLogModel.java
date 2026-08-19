
package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class DeviceControlLogModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4306240324629153073L;

    private String id;

    @ApiModelProperty(value = "操作人id.")
    private String creatorId;

    @ApiModelProperty(value = "操作人姓名.")
    private String name;

    @ApiModelProperty(value = "操作人工号.")
    private String staffid;

    @ApiModelProperty(value = "操作时间.")
    private Date createTime;

    @ApiModelProperty(value = "操作渠道.")
    private String channel;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "操作结果.")
    private String result;
}
