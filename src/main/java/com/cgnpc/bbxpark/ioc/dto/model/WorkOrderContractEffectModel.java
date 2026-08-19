package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏工单异常分析
 */
@Data
public class WorkOrderContractEffectModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单来源名称")
    private String name;

    @ApiModelProperty(value = "异常工单数量")
    private Integer errorNum;

    @ApiModelProperty(value = "未发现异常工单数量")
    private Integer noErrorNum;
}
