package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏材料使用排行分析
 */
@Data
public class MaterialUsageRankModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "材料名称")
    private String name;

    @ApiModelProperty(value = "库存数量")
    private Integer totalNum;

    @ApiModelProperty(value = "使用比例%")
    private Double useRate;
}
