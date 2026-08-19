
package com.cgnpc.bbxpark.device.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据模型实体
 */
@Data
@TableName("bbx_iot_thing_model")
public class ThingModel implements Serializable {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *id.
	 **/
	private Long id;
	/**
	 *物模型.
	 **/
	private String model;
	/**
	 *产品key.
	 **/
	private String productKey;
}
