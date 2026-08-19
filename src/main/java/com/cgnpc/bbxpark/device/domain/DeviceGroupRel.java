
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 设备分组关系数据模型实体
 * @author huangyongtao
 * @date 2024/8/12 11:45
 */
@Data
@TableName("bbx_device_group_rel")
public class DeviceGroupRel extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*分组id.
	**/
	private Long groupId;
	/**
	*设备id.
	**/
	private Long deviceId;
}
