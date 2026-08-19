package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会议室-设备分页列表
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 10:07
 */
@Data
public class MeetingRoomDeviceModel implements Serializable {
    @ApiModelProperty(value = "设备id.")
    private Long deviceId;
    @ApiModelProperty(value = "iot设备id.")
    private String iotDeviceDn;
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;
    @ApiModelProperty(value = "产品key")
    private String productKey;
    @ApiModelProperty(value = "在线状态.")
    private Boolean online;
}
