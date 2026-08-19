package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "人员通行趋势(7日)")
public class PersonPassTrendModel {
    @ApiModelProperty(value = "日期(MM-dd)")
    private String date;

    @ApiModelProperty(value = "次数")
    private Integer count;
}
