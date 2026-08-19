
package com.cgnpc.bbxpark.invitation.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApprovalTaskPageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4633716801413661531L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "业务id.")
    private Long businessId;

    @ApiModelProperty(value = "状态:10->待审批;20->审批通过;30->审批不通过.")
    private Integer status;

    @ApiModelProperty(value = "扩展内容.")
    private String extendContent;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Integer deleted;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "乐观锁.")
    private Integer revision;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

}
