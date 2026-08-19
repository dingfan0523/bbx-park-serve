
package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_station")
public class Station extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4325268336521229921L;
	/**
	*关联空间ID.
	**/
	private Long spaceId;
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
	*部门id.
	**/
	private String departmentId;
	/**
	*分配人id.
	**/
	private String assignerId;
	/**
	*分配人名称.
	**/
	private String assignerName;
	/**
	*分配人工号.
	**/
	private String assignerStaffid;
}
