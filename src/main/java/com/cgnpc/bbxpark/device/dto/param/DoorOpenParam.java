package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/8/20 16:48
 */
@Data
public class DoorOpenParam {
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "渠道:app->移动端;pc->用户端;screen->大屏")
    private String channel;
}
