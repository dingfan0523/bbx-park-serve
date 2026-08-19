
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备标签数据模型实体
 */
@Data
@TableName("bbx_device_label")
public class DeviceLabel  extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*标签编码.
	**/
	private String labelCode;
	/**
	*标签名称.
	**/
	private String labelName;
	/**
	*标签颜色.
	**/
	private String labelColour;
	/**
	*标签描述.
	**/
	private String labelDescribe;
	/**
	 *员工号.
	 **/
	private String staffId;

}
