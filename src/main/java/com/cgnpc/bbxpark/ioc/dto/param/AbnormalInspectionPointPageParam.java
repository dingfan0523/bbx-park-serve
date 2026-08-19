package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AbnormalInspectionPointPageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "巡检点id")
    private Long id;
}
