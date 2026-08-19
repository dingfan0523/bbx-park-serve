
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_compartment_device")
public class CompartmentDevice extends BaseExEntity implements Serializable {

	/**
	*包间id.
	**/
	private Long compartmentId;
	/**
	*设施id.
	**/
	private Long deviceId;
	/**
	*设施名称.
	**/
	private String deviceName;
	/**
	*租户id.
	**/
	private Long tenantId;

}
