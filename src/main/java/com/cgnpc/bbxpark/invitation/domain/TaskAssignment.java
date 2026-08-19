
package com.cgnpc.bbxpark.invitation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_task_assignment")
public class TaskAssignment extends BaseExEntity implements Serializable {
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
	*用户id.
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
	*是否审批(0->是;1->否).
	**/
	private Integer approved = 1;
	/**
	*乐观锁.
	**/
	private Integer revision;

}
