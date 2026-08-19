package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏餐厅供应商信息
 */
@Data
public class RestaurantSupplierModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "供应商名称.")
    private String name;

    @ApiModelProperty(value = "供货次数.")
    private Integer supplyNum;

    @ApiModelProperty(value = "供货品类.")
    private Integer categoryNum;

    @ApiModelProperty(value = "供货时长（年）.")
    private Double years;
}

