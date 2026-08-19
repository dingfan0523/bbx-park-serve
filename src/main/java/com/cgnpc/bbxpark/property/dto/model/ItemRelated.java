package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/11/5 13:40
 */
@Data
public class ItemRelated implements Serializable {
    @ApiModelProperty(value = "关联id")
    private Long relatedId;
    @ApiModelProperty(value = "关联名称")
    private String relatedName;
}
