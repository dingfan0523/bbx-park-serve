package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CarTravelRecordPageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

    @ApiModelProperty(value = "车牌号")
    private String carPlate;

    @ApiModelProperty(value = "驾驶员")
    private String driverName;
}
