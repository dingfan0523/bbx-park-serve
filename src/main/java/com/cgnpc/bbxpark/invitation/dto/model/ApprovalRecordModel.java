
package com.cgnpc.bbxpark.invitation.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class ApprovalRecordModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4962128327074425678L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "业务id.")
    private Long businessId;

    @ApiModelProperty(value = "审批人id.")
    private String userId;

    @ApiModelProperty(value = "工号.")
    private String staffid;

    @ApiModelProperty(value = "名称.")
    private String userName;

    @ApiModelProperty(value = "状态:20->审批通过;30->审批不通过.")
    private Integer status;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "设备名称")
    private String extend1;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;
}
