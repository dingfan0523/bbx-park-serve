
package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_space_manager")
public class SpaceManager extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4531857809869994028L;
	/**
	*关联空间ID.
	**/
	private Long spaceId;
	/**
	 *人员ID.
	 **/
	private String userId;
	/**
	*人员类型.
	**/
	private Integer type;
	/**
	*人员姓名.
	**/
	private String name;
	/**
	 * 工号.
	 */
	private String staffid;
	/**
	*手机号.
	**/
	private String phone;
	/**
	*备注.
	**/
	private String remark;
}
