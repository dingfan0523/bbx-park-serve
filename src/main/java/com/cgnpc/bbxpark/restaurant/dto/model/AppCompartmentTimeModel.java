package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 包间营业时间业务数据模型
 * @author dingfan
 * @date 2024/7/31 15:08
 */
@Data
public class AppCompartmentTimeModel implements Serializable {
    @ApiModelProperty(value = "用餐类型（字典）.")
    private String type;
    @ApiModelProperty(value = "开始时间.")
    private String startTime;
    @ApiModelProperty(value = "结束时间.")
    private String endTime;
}
