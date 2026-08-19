package com.cgnpc.bbxpark.invitation.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/8/1 9:57
 */
@Data
public class ExtendContent implements Serializable {
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
}
