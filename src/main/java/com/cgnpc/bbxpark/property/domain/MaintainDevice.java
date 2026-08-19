package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 维保设备关联数据模型实体
 * @author huangyongtao
 * @date 2025/10/16 15:16
 */
@Data
@TableName("bbx_maintain_device")
public class MaintainDevice extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*维保id.
	**/
	private Long maintainId;
	/**
	*设备id.
	**/
	private Long deviceId;
	/**
	*任务组.
	**/
	private Integer taskGroup;
}
