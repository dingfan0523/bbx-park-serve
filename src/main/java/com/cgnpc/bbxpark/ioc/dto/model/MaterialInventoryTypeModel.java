package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏材料库存类型分析
 */
@Data
public class MaterialInventoryTypeModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型编码")
    private Integer code;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "库存状态;（1：库存充足；2：库存不足；3：缺货）")
    private Integer status;

    @ApiModelProperty(value = "类型数量")
    private Integer totalNum;
}
