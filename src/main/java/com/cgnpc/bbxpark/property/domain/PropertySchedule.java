
package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 物业排班数据模型实体
 */
@Data
@TableName("bbx_property_schedule")
public class PropertySchedule extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*分组名称.
	**/
	private String name;
	/**
	*分组描述.
	**/
	private String remark;
}
