package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏材料工单使用分析
 */
@Data
public class MaterialWorkUseModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单来源名称")
    private String name;

    @ApiModelProperty(value = "使用数量")
    private Integer totalNum;
}
