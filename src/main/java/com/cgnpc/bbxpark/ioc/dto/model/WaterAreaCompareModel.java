package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "区域能耗对比(用水)")
public class WaterAreaCompareModel {
    @ApiModelProperty(value = "支路ID")
    private Long id;

    @ApiModelProperty(value = "支路名称")
    private String branchName;

    @ApiModelProperty(value = "用水量")
    private BigDecimal water;

    @ApiModelProperty(value = "是否存在下级")
    private Boolean hasChildren;
}
