package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜品类型业务数据模型
 * @author dingfan
 * @date 2024/7/18 16:21
 */
@Data
public class DishesTypeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型名称")
    private String label;

    @ApiModelProperty(value = "类型值")
    private String value;
}
