package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
@ApiModel(value = "办公空间概览模型")
public class OfficeSpaceOverviewModel {
    @ApiModelProperty(value = "办公总面积")
    private BigDecimal officeArea;
    @ApiModelProperty(value = "人均办公面积")
    private Double avgOfficeArea;
    @ApiModelProperty(value = "工位利用率")
    private Double utilizationRate;
}