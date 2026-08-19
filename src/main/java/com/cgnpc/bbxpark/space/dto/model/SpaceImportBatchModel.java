
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class SpaceImportBatchModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4331917379864072395L;

    @ApiModelProperty(value = "标识.")
    private Long id;

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

    @ApiModelProperty(value = "乐观锁.")
    private String revision;

    @ApiModelProperty(value = "创建人.")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间.")
    private Date createDate;

    @ApiModelProperty(value = "更新人.")
    private Long modifyUserId;

    @ApiModelProperty(value = "更新时间.")
    private Date modifyDate;


}
