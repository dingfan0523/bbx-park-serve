package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TaskItemListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4775400974304504761L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "任务id.")
    private Long taskId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "主键id集合.")
    private List<Long> ids;
}
