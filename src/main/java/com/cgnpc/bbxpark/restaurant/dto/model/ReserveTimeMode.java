package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 包间预定时间业务数据模型
 * @author dingfan
 * @date 2024/8/5 10:21
 */
@Data
public class ReserveTimeMode implements Serializable {
    @ApiModelProperty(value = "预定id")
    private Long id;
    @ApiModelProperty(value = "预定开始时间.")
    private Date reserveStartTime;
    @ApiModelProperty(value = "预定结束时间.")
    private Date reserveEndTime;
}
