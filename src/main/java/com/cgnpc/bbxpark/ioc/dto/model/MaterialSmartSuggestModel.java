package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏材料智能分析分析
 */
@Data
public class MaterialSmartSuggestModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "材料名称")
    private String name;

    @ApiModelProperty(value = "预警值数量")
    private Integer warningNum;

    @ApiModelProperty(value = "当前库存数量")
    private Integer quantityNum;

    @ApiModelProperty(value = "周转天数")
    private Integer turnoverDay;

    @ApiModelProperty(value = "行动建议")
    private String suggest;

}
