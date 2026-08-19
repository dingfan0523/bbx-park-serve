
package com.cgnpc.bbxpark.device.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 设备分组数据模型实体
 * @author huangyongtao
 * @date 2024/8/12 11:42
 */
@Data
@TableName("bbx_device_group")
public class DeviceGroup extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
	/**
	*分组编码.
	**/
	private String groupCode;
	/**
	*分组名称.
	**/
	private String groupName;
	/**
	*排序序号.
	**/
	private Integer sortOrder;
	/**
	*分组父级id.
	**/
	private Long groupParentId;
	/**
	*分组描述.
	**/
	private String groupDescribe;
}
