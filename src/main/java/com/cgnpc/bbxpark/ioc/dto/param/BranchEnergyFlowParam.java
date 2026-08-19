package com.cgnpc.bbxpark.ioc.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/24
 * @desc 支路能耗流向图查询参数
 */
@Data
public class BranchEnergyFlowParam implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    @ApiModelProperty(value = "支路id")
    private Long branchId;

    @ApiModelProperty(value = "支路类型(electricity-电water-水gas-燃气)")
    private String branchType;

    @ApiModelProperty(value = "读数来源(person:人工抄表 auto:自动上报)")
    private String meterMethod;

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;
}
