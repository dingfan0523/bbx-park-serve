package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : yangjian
 * @date: 2026/2/9−17:08
 * @Description: com.cgnpc.bbxpark.ioc.dto.model
 * @version: 1.0
 */
@Data
@ApiModel(value = "设备属性模型")
public class DeviceProperty {

    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "属性值")
    private String value;
}
