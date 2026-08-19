
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_compartment_combo")
public class CompartmentCombo extends BaseExEntity implements Serializable{

	/**
	*包间id.
	**/
	private Long compartmentId;
	/**
	*套餐id.
	**/
	private Long comboId;
	/**
	*租户id.
	**/
	private Long tenantId;

}
