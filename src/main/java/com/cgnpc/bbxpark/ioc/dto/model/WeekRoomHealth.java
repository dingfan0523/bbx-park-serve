package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "周内会议室健康度")
public class WeekRoomHealth {
    @ApiModelProperty(value = "时间")
    private Date date;
    @ApiModelProperty(value = "数量")
    private Integer count;
}
