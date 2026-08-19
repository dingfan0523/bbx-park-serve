package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CarRentRecordPageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

    @ApiModelProperty(value = "租车类型")
    private String rentType;

    @ApiModelProperty(value = "状态")
    private String instanceStatus;

    @ApiModelProperty(value = "租车时间")
    private Date rentTime;
}
