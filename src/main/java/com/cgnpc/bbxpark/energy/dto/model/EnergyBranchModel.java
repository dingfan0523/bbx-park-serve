package com.cgnpc.bbxpark.energy.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 支路管理模型
 */
@Data
public class EnergyBranchModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    @ApiModelProperty(value = "主键ID")
    private Long id ;
    /** 支路名称 */
    @ApiModelProperty(value = "支路名称")
    private String branchName ;
    /** 支路编码，全局唯一 */
    @ApiModelProperty(value = "支路编码，全局唯一")
    private String branchCode ;
    /** 支路类型(electricity-电water-水gas-燃气) */
    @ApiModelProperty(value = "支路类型(electricity-电water-水gas-燃气)")
    private String branchType ;
    /** 父支路 ID，一级支路为 NULL */
    @ApiModelProperty(value = "父支路 ID，一级支路为 NULL")
    private Long parentId ;
    /** 排序字段 */
    @ApiModelProperty(value = "排序字段")
    private Integer sortOrder ;
    /** 描述说明 */
    @ApiModelProperty(value = "描述说明")
    private String description ;
    /** 启用状态（1:启用0:禁用） */
    @ApiModelProperty(value = "启用状态（1:启用0:禁用）")
    private Integer status ;
    /** 删除（1:正常；0：已删除） */
    @ApiModelProperty(value = "删除（1:正常；0：已删除）")
    private Integer deleted ;
    @ApiModelProperty(value = "子支路")
    private List<EnergyBranchModel> children;
    @ApiModelProperty(value = "总耗能量")
    private BigDecimal totalPower;
    private Long tenantId;
}
