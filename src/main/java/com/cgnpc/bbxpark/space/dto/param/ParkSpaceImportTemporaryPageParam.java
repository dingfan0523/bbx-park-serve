
package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ParkSpaceImportTemporaryPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3538617042425112654L;

    private Long id;

    @ApiModelProperty(value = "上级空间编码.")
    private String parentSpaceCode;

    @ApiModelProperty(value = "空间编码.")
    private String spaceCode;

    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "空间地址.")
    private String spaceAddr;

    @ApiModelProperty(value = "空间描述.")
    private String spaceDesc;

    @ApiModelProperty(value = "批次.")
    private String batchCode;

    @ApiModelProperty(value = "所属空间ID.")
    private Long parentSpaceId;

    @ApiModelProperty(value = "排序序号.")
    private String orderCode;

    @ApiModelProperty(value = "状态0默认1失败2成功.")
    private String importStatus;

    @ApiModelProperty(value = "失败错误描述.")
    private String importErrorDesc;

    @ApiModelProperty(value = "所属园区ID-租户号.")
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
