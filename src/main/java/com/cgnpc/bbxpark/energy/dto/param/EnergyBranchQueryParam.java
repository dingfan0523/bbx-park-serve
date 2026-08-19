package com.cgnpc.bbxpark.energy.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 支路查询参数
 */
@Data
public class EnergyBranchQueryParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    /** 支路类型(electricity-电water-水gas-燃气) */
    @ApiModelProperty(value = "支路类型(electricity-电water-水gas-燃气)")
    private String branchType ;

    /** 支路名称 */
    @ApiModelProperty(value = "支路名称")
    private String branchName ;

    @ApiModelProperty(value = "支路编码")
    private String branchCode ;

    /** 启用状态（1:启用0:禁用） */
    @ApiModelProperty(value = "启用状态（1:启用0:禁用）")
    private Integer status ;

    private Integer deleted;
}
