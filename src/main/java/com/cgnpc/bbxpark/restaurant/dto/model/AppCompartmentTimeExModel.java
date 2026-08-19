package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 包间营业时间业务数据模型
 * @author dingfan
 * @date 2024/7/31 15:08
 */
@Data
public class AppCompartmentTimeExModel extends AppCompartmentTimeModel{
    @ApiModelProperty(value = "状态:0->可预订;1->时段已过;2->已被预定")
    private Integer status;
}
