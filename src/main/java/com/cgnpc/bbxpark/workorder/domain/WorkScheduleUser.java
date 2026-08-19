
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 工单排班人员数据模型实体
 * @author huangyongtao
 * @date 2025/11/4 16:21
 */
@Data
@TableName("bbx_work_schedule_user")
public class WorkScheduleUser extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*工单id.
	**/
	private Long workId;
	/**
	*物业分组id.
	**/
	private Long scheduleId;
	/**
	*物业分组名称.
	**/
	private String scheduleName;
	/**
	*是否负责人:0->是;1->否.
	**/
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