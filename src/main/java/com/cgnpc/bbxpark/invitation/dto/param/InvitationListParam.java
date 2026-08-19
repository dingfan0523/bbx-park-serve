
package com.cgnpc.bbxpark.invitation.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 邀约列表参数模型
 * @author huangyongtao
 * @date 2025/8/1 14:04
 */
@Data
public class InvitationListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "结束时间.")
    private Date endTime;

    @ApiModelProperty(value = "邀约人id.")
    private String inviteUid;

    @ApiModelProperty(value = "邀约人名称.")
    private String inviteUname;

    @ApiModelProperty(value = "邀约人工号.")
    private String inviteStaffid;

    @ApiModelProperty(value = "接待人id.")
    private String receiveUid;

    @ApiModelProperty(value = "接待人名称.")
    private String receiveUname;

    @ApiModelProperty(value = "接待人工号.")
    private String receiveStaffid;

    @ApiModelProperty(value = "到访事由;1：参观调研；2：参加会议；3：业务培训.")
    private Integer visitReasonType;

    @ApiModelProperty(value = "接待类型;1：本人接待；2：他人接待.")
    private Integer receiveType;

    @ApiModelProperty(value = "实际开始时间.")
    private Date realStartTime;

    @ApiModelProperty(value = "实际结束时间.")
    private Date realEndTime;

    @ApiModelProperty(value = "邀约状态;10：待审批；20：待来访；30：访问中；40：已结束.")
    private Integer inviteStatus;

    @ApiModelProperty(value = "结束原因;1：邀约取消；2：审批不通过；3：超时未审批；4：正常结束.")
    private Integer endReason;

    @ApiModelProperty(value = "取消说明.")
    private String cancelRemark;

    @ApiModelProperty(value = "邀约说明.")
    private String remark;

    @ApiModelProperty(value = "访客名称.")
    private String visitorName;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "邀约时间开始.")
    private Date createTimeStart;

    @ApiModelProperty(value = "邀约时间结束.")
    private Date createTimeEnd;
}
