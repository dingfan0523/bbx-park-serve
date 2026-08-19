package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏工单区域分布
 */
@Data
public class WorkOrderScreenSpaceAnalysisModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空间名称")
    private String name;

    @ApiModelProperty(value = "空间id")
    private Long id;

    @ApiModelProperty(value = "数量")
    private Integer totalNum;

    @ApiModelProperty(value = "是否有下级空间")
    private Boolean hasChildren;

    @ApiModelProperty(value = "工单来源集合")
    private List<WorkOrderSourceAnalysisModel> sourceModels;

}
