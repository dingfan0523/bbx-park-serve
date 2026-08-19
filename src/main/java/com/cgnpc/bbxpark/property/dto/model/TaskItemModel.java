package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class TaskItemModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3839633986163333043L;
    @ApiModelProperty(value = "id.")
    private Long id;
    @ApiModelProperty(value = "任务id.")
    private Long taskId;
    @ApiModelProperty(value = "空间id.")
    private Long spaceId;
    @ApiModelProperty(value = "空间名称.")
    private String spaceName;
    @ApiModelProperty(value = "名称.")
    private String name;
    @ApiModelProperty(value = "内容.")
    private String content;
    @ApiModelProperty(value = "删除状态(0->已删;1->未删).")
    private Integer deleted = 1;
}
