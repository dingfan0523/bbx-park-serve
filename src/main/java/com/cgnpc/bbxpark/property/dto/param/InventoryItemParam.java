
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class InventoryItemParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4263473195706217077L;

    @ApiModelProperty(value = "盘点计划id.")
    private Long inventoryId;
    @ApiModelProperty(value = "类型:1->器材耗材;2->设备设施")
    private Integer type;
    @Length(max = 255)
    @ApiModelProperty(value = "相关联id(多个以英文逗号隔开).")
    private String relatedId;
    @Length(max = 255)
    @ApiModelProperty(value = "内容.")
    private String content;
}
