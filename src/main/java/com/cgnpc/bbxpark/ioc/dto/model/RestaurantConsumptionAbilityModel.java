package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "消费能力分析")
public class RestaurantConsumptionAbilityModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间(MM-dd)")
    private String time;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "消费金额（单位：元）")
    private Double amount;
}