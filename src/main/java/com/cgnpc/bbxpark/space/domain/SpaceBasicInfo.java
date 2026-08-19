
package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@TableName("bbx_space_basic_info")
public class SpaceBasicInfo extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3660648947362932452L;

	/**
	 *关联空间ID.
	 **/
	private Long spaceId;
	/**
	*空间类型.
	**/
	private Integer type;
	/**
	*使用单位id.
	**/
	private String departmentId;
    /**
     * 使用单位名称
     */
    private String departmentName;
	/**
	*面积（㎡）.
	**/
	private BigDecimal area;
	/**
	*空间容量(人).
	**/
	private Integer capacity;
	/**
	*工位管理员id.
	**/
	private String managerId;
	/**
	*空间用途.
	**/
	private String purpose;
}
