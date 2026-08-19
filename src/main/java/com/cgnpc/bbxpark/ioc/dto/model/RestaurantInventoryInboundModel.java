package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏餐料入库分析模型
 */
@Data
public class RestaurantInventoryInboundModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间（MM）")
    private String time;

    @ApiModelProperty(value = "物料品类.")
    private String category;

    @ApiModelProperty(value = "入库数量.")
    private Double inboundQty;

}

