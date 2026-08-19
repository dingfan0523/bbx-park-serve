
package com.cgnpc.bbxpark.invitation.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ApprovalRecordParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4080488345255877752L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "任务id.")
    private Long taskId;

    @ApiModelProperty(value = "业务id.")
    private Long businessId;

    @ApiModelProperty(value = "审批人id.")
    private String userId;

    @Length(max = 32)
    @ApiModelProperty(value = "工号.")
    private String staffid;

    @Length(max = 32)
    @ApiModelProperty(value = "名称.")
    private String userName;

    @ApiModelProperty(value = "状态:20->审批通过;30->审批不通过.")
    private Integer status;

    @Length(max = 200)
    @ApiModelProperty(value = "备注.")
    private String remark;

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

    @ApiModelProperty(value = "id集合.")
    private List<Long> ids;
}
