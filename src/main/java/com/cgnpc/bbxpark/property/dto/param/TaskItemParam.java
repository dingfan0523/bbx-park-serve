package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class TaskItemParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4117368561001527091L;

    @ApiModelProperty(value = "任务id.")
    private Long taskId;
    @ApiModelProperty(value = "空间id.")
    private Long spaceId;
    @Length(max = 100)
    @ApiModelProperty(value = "名称.")
    private String name;
    @Length(max = 255)
    @ApiModelProperty(value = "内容.")
    private String content;
}
