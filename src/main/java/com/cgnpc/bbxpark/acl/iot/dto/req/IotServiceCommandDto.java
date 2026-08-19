package com.cgnpc.bbxpark.acl.iot.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/6/26 8:52
 */
@ApiModel(description = "IOT服务调用 条件")
@Data
public class IotServiceCommandDto {
    @ApiModelProperty(name =  "deviceId")
    @NotBlank(message = "设备编码不能为空")
    private String deviceId;
    @ApiModelProperty(name =  "service")
    @NotBlank(message = "服务不能空")
    private String service;
    @ApiModelProperty(name =  "args")
    @NotEmpty(message = "服务参数不能为空")
    private Map<String ,Object> args;
}
