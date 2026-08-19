package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 移动端-简单的预约模型
 * @author dingfan
 * @date 2024/8/15 9:45
 */
@Data
public class AppSimpleReserveModel {
    @ApiModelProperty(value = "预约id")
    private Long id;
    @ApiModelProperty(value = "餐厅名称")
    private String restaurantName;
    @ApiModelProperty(value = "包间名称")
    private String compartmentName;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
