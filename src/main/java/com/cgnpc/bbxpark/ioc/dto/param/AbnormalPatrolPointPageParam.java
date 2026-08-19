package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AbnormalPatrolPointPageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "巡更点名称")
    private String name;

    @ApiModelProperty(value = "巡更点类型")
    private Integer type;
}
