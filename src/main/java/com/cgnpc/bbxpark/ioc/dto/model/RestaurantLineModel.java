package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏餐线信息
 */
@Data
public class RestaurantLineModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "餐线id.")
    private Long id;

    @ApiModelProperty(value = "餐线名称.")
    private String name;

    @ApiModelProperty(value = "餐线状态：营业中/休息中")
    private String status;

    @ApiModelProperty(value = "餐线工作状态：空闲,繁忙")
    private String workStatus;

    @ApiModelProperty(value = "预计排队时间")
    private String waitingTime;

    @ApiModelProperty(value = "所属楼层物模型编码")
    private String sslcCode;

    @ApiModelProperty(value = "餐线营业时间集合")
    private List<RestaurantLineTimeModel> times;

}

