
package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 物业排班人员数据模型实体
 */
@Data
@TableName("bbx_property_schedule_user")
public class PropertyScheduleUser extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*排班id.
	**/
	private Long scheduleId;
	/**
	 * 是否负责人(1->否;0->是).
	 */
	private Integer manager;
    /**
     *人员id.
     **/
    private String userId;
	/**
	*人员名称.
	**/
	private String userName;
	/**
	*人员工号.
	**/
	private String staffid;
	/**
	*联系方式.
	**/
	private String phone;
	/**
	*部门id.
	**/
	private String departmentId;
	/**
	*部门名称.
	**/
	private String departmentName;
}
