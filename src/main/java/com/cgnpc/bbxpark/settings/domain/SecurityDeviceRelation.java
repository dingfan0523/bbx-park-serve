
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 安全管理员设备关联数据模型实体
 * @author huangyongtao
 * @date 2025/7/31 17:37
 */
@Data
@TableName("bbx_security_device_relation")
public class SecurityDeviceRelation extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*安全管理id.
	**/
	private Long securityManageId;
	/**
	*设备id.
	**/
	private Long deviceId;

}
