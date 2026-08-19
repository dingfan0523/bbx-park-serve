package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏材料库存健康度分析
 */
@Data
public class MaterialInventoryHealthModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "健康度名称")
    private String name;

    @ApiModelProperty(value = "健康度编码")
    private Integer code;

    @ApiModelProperty(value = "健康度数量")
    private Integer totalNum;

    @ApiModelProperty(value = "数量比例%")
    private Double numRate;

}
