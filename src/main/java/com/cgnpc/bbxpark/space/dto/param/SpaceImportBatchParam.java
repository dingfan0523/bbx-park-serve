
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SpaceImportBatchParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3150188268466289239L;

//    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "标识.")
    private Long id;

    @Length(max = 255)
    @ApiModelProperty(value = "批次.")
    private String batchCode;

    @ApiModelProperty(value = "导入总数.")
    private Integer importAllNum;

    @ApiModelProperty(value = "导入成功数.")
    private Integer importSuccessNum;

    @ApiModelProperty(value = "导入失败数.")
    private Integer importErrorNum;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @Length(max = 32)
    @ApiModelProperty(value = "乐观锁.")
    private String revision;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "标识集合.")
    private List<Long> ids;
}
