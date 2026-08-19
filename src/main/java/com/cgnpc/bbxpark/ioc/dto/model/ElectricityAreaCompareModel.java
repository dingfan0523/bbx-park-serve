package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "区域能耗对比")
public class ElectricityAreaCompareModel {
    @ApiModelProperty(value = "支路ID")
    private Long id;

    @ApiModelProperty(value = "支路名称")
    private String branchName;

    @ApiModelProperty(value = "用电量")
    private BigDecimal electricity;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "是否存在下级")
    private Boolean hasChildren;
}
