package com.cgnpc.bbxpark.energy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 能量支路实体类
 */
@Data
@TableName("bbx_energy_branch")
public class EnergyBranch extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 支路名称 */
    private String branchName;
    /** 支路编码，全局唯一 */
    private String branchCode;
    /** 支路类型(electricity-电water-水gas-燃气) */
    private String branchType;
    /** 父支路 ID，一级支路为 NULL */
    private Long parentId;
    /** 排序字段 */
    private Integer sortOrder;
    /** 描述说明 */
    private String description;
    /** 启用状态（1:启用0:禁用） */
    private Integer status;
    /** 乐观锁 */
    private String revision;
}
