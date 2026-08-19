
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 工单流转数据模型实体
 */
@Data
@TableName("bbx_work_order_roman")
public class WorkOrderRoman extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*工单id.
	**/
	private Long workOrderId;
	/**
	*流转状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）.
	**/
	private Integer romanStatus;
    /**
     *操作人id.
     **/
    private String operatorId;
	/**
	*操作人名称.
	**/
	private String operatorName;
	/**
	 * 操作人工号
	 */
	private String operatorStaffid;
	/**
	*操作的值.
	**/
	private String operatorValue;
	/**
	*操作.
	**/
	private String operator;
	/**
	*说明备注.
	**/
	private String remark;
	/**
	*冗余字段1（分配人id）.
	**/
	private String redundancyOne;
	/**
	*冗余字段2（分配人名称）.
	**/
	private String redundancyTwo;
	/**
	*冗余字段3（分配人工号）.
	**/
	private String redundancyThree;
	/**
	*冗余字段4（处理结果）
	**/
	private String redundancyFour;
	/**
	 * 冗余字段5(处理图片)
	 */
	private String redundancyFive;
	/**
	*展示：1，不展示：0
	**/
	private String appExhibition;
}
