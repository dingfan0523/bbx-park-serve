package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "便民班车运行组织机构统计")
public class ShuttleRunOrgStatModel {
    @ApiModelProperty(value = "运行组织机构")
    private String runOrg;

    @ApiModelProperty(value = "次数")
    private Integer count;

    @ApiModelProperty(value = "占比(%)")
    private BigDecimal ratio;
}
