package com.cgnpc.bbxpark.energy.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 能源支路新增入参
 */
@Data
public class EnergyBranchParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id ;

    /** 支路名称 */
    @ApiModelProperty(value = "支路名称")
    @NotNull(groups = Default.class, message = "支路名称不能为空")
    private String branchName ;
    /** 支路编码，全局唯一 */
    @ApiModelProperty(value = "支路编码，全局唯一")
    @NotNull(groups = Default.class, message = "支路编码不能为空")
    private String branchCode ;
    /** 支路类型((electricity-电water-水gas-燃气) ) */
    @ApiModelProperty(value = "支路类型(electricity-电water-水gas-燃气)")
    private String branchType ;
    /** 父支路 ID，一级支路为 NULL */
    @ApiModelProperty(value = "父支路 ID，一级支路为 NULL")
    private Long parentId ;
    /** 排序字段 */
    @ApiModelProperty(value = "排序字段")
    @NotNull(groups = Default.class, message = "排序字段不能为空")
    private Integer sortOrder ;
    /** 描述说明 */
    @ApiModelProperty(value = "描述说明")
    private String description ;
    @ApiModelProperty(value = "启用状态（1:启用0:禁用）")
    private Integer status;
}
