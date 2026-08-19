package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 餐线菜品入参
 */
@Data
public class RestaurantLinePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "餐线id.")
    private Long lineId;

    @ApiModelProperty(value = "餐线营业时间类型.")
    private String timeType;
}