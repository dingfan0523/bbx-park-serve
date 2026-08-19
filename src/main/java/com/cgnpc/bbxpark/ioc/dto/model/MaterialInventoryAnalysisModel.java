package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏材料库存分析
 */
@Data
public class MaterialInventoryAnalysisModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "库存数量")
    private Integer totalNum;

    @ApiModelProperty(value = "库存类型集合")
    private List<MaterialInventoryTypeModel> inventoryTypeModels;

}
