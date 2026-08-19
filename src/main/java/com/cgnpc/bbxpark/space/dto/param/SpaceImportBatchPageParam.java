
package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class SpaceImportBatchPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3812706722467331354L;

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
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

}
