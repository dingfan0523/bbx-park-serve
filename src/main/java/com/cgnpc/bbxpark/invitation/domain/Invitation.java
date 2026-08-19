
package com.cgnpc.bbxpark.invitation.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 邀约数据模型实体
 * @author huangyongtao
 * @date 2025/8/1 13:44
 */
@Data
@TableName("bbx_invitation")
public class Invitation extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*开始时间.
	**/
	private Date startTime;
	/**
	*结束时间.
	**/
	private Date endTime;
	/**
	*邀约人id.
	**/
	private String inviteUid;
	/**
	*邀约人名称.
	**/
	private String inviteUname;
	/**
	*邀约人工号.
	**/
	private String inviteStaffid;
	/**
	*接待人id.
	**/
	private String receiveUid;
	/**
	*接待人名称.
	**/
	private String receiveUname;
	/**
	*接待人工号.
	**/
	private String receiveStaffid;
	/**
	*到访事由;1：参观调研；2：参加会议；3：业务培训.
	**/
	private Integer visitReasonType;
	/**
	*接待类型;1：本人接待；2：他人接待.
	**/
	private Integer receiveType;
	/**
	*实际开始时间.
	**/
	private Date realStartTime;
	/**
	*实际结束时间.
	**/
	private Date realEndTime;
	/**
	*邀约状态;10：待审批；20：待来访；30：访问中；40：已结束.
	**/
	private Integer inviteStatus;
	/**
	*结束原因;1：邀约取消；2：审批不通过；3：超时未审批；4：正常结束.
	**/
	private Integer endReason;
	/**
	 *取消说明.
	 **/
	private String cancelRemark;
	/**
	*邀约说明.
	**/
	private String remark;
	/**
	*乐观锁.
	**/
	@Version
	@TableField(fill = FieldFill.INSERT)
	private Integer revision;

}
