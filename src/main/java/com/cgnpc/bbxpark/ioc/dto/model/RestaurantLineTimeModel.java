package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏餐线供餐时间
 */
@Data
public class RestaurantLineTimeModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "餐线营业时间id")
    private Long id;

    @ApiModelProperty(value = "餐线id.")
    private Long lineId;

    @ApiModelProperty(value = "用餐类型（早餐，午餐，晚餐，夜宵）.")
    private String type;

    @ApiModelProperty(value = "开始时间.")
    private String startTime;

    @ApiModelProperty(value = "结束时间.")
    private String endTime;

    @ApiModelProperty(value = "是否选中.")
    private Boolean selected;

}

