
package com.cgnpc.bbxpark.invitation.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TaskAssignmentListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4107240269123684245L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "任务id.")
    private Long taskId;

    @ApiModelProperty(value = "业务id.")
    private Long businessId;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "工号.")
    private String staffid;

    @ApiModelProperty(value = "名称.")
    private String userName;

    @ApiModelProperty(value = "是否审批(0->是;1->否).")
    private Integer approved = 1;

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

    @ApiModelProperty(value = "id集合.")
    private List<Long> ids;
}
