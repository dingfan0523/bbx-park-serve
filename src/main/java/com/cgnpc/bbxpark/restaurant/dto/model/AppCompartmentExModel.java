package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 包间扩展业务数据模型
 * @author dingfan
 * @date 2024/8/2 17:19
 */
@Data
public class AppCompartmentExModel implements Serializable {
    @ApiModelProperty(value = "包间id.")
    private Long id;
    @ApiModelProperty(value = "包间名称.")
    private String compartmentName;
    @ApiModelProperty(value = "营业时间")
    private List<AppCompartmentTimeExModel> timeList;
    @ApiModelProperty(value = "套餐集合")
    private List<AppComboModel> comboList;
}
