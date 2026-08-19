package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/11/5 14:33
 */
@Data
public class InventoryPlanItemParam implements Serializable {
    @ApiModelProperty(value = "计划id.")
    private Long id;
    @ApiModelProperty(value = "关联资产id集合")
    private List<Long> relatedIdList;
}
