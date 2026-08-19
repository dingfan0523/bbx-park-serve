
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备标签实体类
 */
@Data
@TableName("bbx_device_label_rel")
public class DeviceLabelRel  extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*标签id;标签编码.
	**/
	private Long labelId;
	/**
	*设备id;设备id.
	**/
	private Long deviceId;

}
