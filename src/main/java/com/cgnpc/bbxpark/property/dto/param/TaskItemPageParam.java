package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class TaskItemPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4545873042598702665L;

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

}
