package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CallTaxiRecordPageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

    @ApiModelProperty(value = "叫车原因")
    private String callReason;

    @ApiModelProperty(value = "使用部门")
    private String department;
}
