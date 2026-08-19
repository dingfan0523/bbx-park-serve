package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShuttleOrderPageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "上车站点")
    private String startStation;

    @ApiModelProperty(value = "下车站点")
    private String endStation;
}
