
package com.cgnpc.bbxpark.invitation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_approval_record")
public class ApprovalRecord extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*任务id.
	**/
	private Long taskId;
	/**
	*业务id.
	**/
	private Long businessId;
	/**
	*审批人id.
	**/
	private String userId;
	/**
	*工号.
	**/
	private String staffid;
	/**
	*名称.
	**/
	private String userName;
	/**
	*状态:20->审批通过;30->审批不通过.
	**/
	private Integer status;
	/**
	*备注.
	**/
	private String remark;
	/**
	 * 扩展字段1
	 */
	private String extend1;
	/**
	*乐观锁.
	**/
	private Integer revision;
}
