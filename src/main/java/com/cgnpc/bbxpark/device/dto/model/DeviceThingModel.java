package com.cgnpc.bbxpark.device.dto.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 设备物模型
 * @author dingfan
 * @version 1.0
 * @date 2025/1/8 9:16
 */
@Data
public class DeviceThingModel {
    @ApiModelProperty(value = "物模型标识")
    private String identifier;
    @ApiModelProperty(value = "物模型名称")
    private String name;
    @ApiModelProperty(value = "数据类型(text、enum、bool)")
    private String dataType;
    @ApiModelProperty(value = "数据格式")
    private JSONObject dataSpecs;
    @ApiModelProperty(value = "入参json")
    private List<DeviceThingModelParam> inputData;
}
