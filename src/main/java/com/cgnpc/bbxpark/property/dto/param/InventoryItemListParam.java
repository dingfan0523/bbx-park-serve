package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InventoryItemListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4100664023072219091L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "任务id.")
    private Long taskId;

    @ApiModelProperty(value = "类型.")
    private Integer type;

    @ApiModelProperty(value = "相关联id(多个以英文逗号隔开).")
    private String relatedId;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "主键id集合.")
    private List<Long> ids;
}
