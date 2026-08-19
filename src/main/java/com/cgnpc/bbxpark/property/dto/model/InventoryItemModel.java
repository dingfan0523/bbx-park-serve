
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class InventoryItemModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3702070062301590092L;

    @ApiModelProperty(value = "任务id.")
    private Long inventoryId;
    @ApiModelProperty(value = "类型:1->器材耗材;2->设备设施")
    private Integer type;
    @ApiModelProperty(value = "关联信息集合")
    private List<ItemRelated> relatedList;
    @ApiModelProperty(value = "内容.")
    private String content;
}
