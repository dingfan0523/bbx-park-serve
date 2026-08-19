package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @create zhaoshuo
 * @time 2024/12/30
 * @desc 场景控制入参
 */
@Data
public class SceneControlParam {

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "服务id")
    private String service;

    @ApiModelProperty(value = "服务参数")
    private Map<String ,Object> args;
}
