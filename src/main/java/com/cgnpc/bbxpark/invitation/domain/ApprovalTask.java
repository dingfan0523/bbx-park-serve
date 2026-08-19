
package com.cgnpc.bbxpark.invitation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_approval_task")
public class ApprovalTask extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*业务id.
	**/
	private Long businessId;
	/**
	*状态:-10->审批被取消;10->待审批;20->审批通过;30->审批不通过.
	**/
	private Integer status;
	/**
	*扩展内容.
	**/
	private String extendContent;
}
