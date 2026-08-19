package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典
 */
@Data
public class DictItemModel implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty(
                value = "字典项标识."
        )
        private Long id;
        @ApiModelProperty(
                value = "值编码."
        )
        private String code;
        @ApiModelProperty(
                value = "创建时间."
        )
        private Date createTime;
        @ApiModelProperty(
                value = "创建者."
        )
        private String creatorId;
        @ApiModelProperty(
                value = "描述."
        )
        private String description;
        @ApiModelProperty(
                value = "字典类型标识."
        )
        private String dictTypeId;

        @ApiModelProperty(
                value = "字典编码."
        )
        private String dictTypeCode;
        @ApiModelProperty(
                value = "是否默认值."
        )
        private Boolean isDefault = false;
        @ApiModelProperty(
                value = "值文本."
        )
        private String label;
        @ApiModelProperty(
                value = "排序号."
        )
        private Integer sortOrder;
        @ApiModelProperty(
                value = "状态，0正常1禁用."
        )
        private Short status;
        @ApiModelProperty(
                value = "修改时间."
        )
        private Date updateTime;
        @ApiModelProperty(
                value = "修改者."
        )
        private String updatorId;
        @ApiModelProperty(
                value = "字典值."
        )
        private String value;
        @ApiModelProperty(
                value = "操作类型：保持：KIP；修改：MOD；新增：ADD；删除：DEL"
        )
        private String operType;
        @ApiModelProperty(
                value = "更新者名称"
        )
        private String updaterName;
        @ApiModelProperty(
                value = "创建者名称"
        )
        private String creatorName;
}
