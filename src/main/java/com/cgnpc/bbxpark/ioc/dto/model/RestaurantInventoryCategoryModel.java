package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏餐料库存分析
 */
@Data
public class RestaurantInventoryCategoryModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物料品类.")
    private String category;

    @ApiModelProperty(value = "库存数量.")
    private Double stockQty;

    @ApiModelProperty(value = "入库数量.")
    private Double inboundQty;


}

