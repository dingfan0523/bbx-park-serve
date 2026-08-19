package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @date 2024/8/5 14:22
 */
@Data
public class AppComboModel implements Serializable {
    @ApiModelProperty(value = "套餐id")
    private Long id;
    @ApiModelProperty(value = "套餐名称")
    private String comboName;
    @ApiModelProperty(value = "套餐描述")
    private String comboDescription;
}
