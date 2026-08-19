package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏餐厅包间实况
 */
@Data
public class RestaurantCompartmentOverviewModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "包间总数")
    private Integer totalNum;

    @ApiModelProperty(value = "今日预定包间数")
    private Integer reservedNum;

    @ApiModelProperty(value = "包间集合")
    private List<Compartment> compartments;

    @Data
    @ApiModel(value = "包间信息")
    public static class Compartment {

        @ApiModelProperty(value = "包间名称")
        private String name;

        @ApiModelProperty(value = "包间图片")
        private String imageUrl;

        @ApiModelProperty(value = "是否预定")
        private Boolean reserved;
    }
}

