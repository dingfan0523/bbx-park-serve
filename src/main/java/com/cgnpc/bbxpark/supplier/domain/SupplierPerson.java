
package com.cgnpc.bbxpark.supplier.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 服务商人员数据模型实体
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Data
@TableName("bbx_supplier_person")
public class SupplierPerson extends BaseExEntity implements Serializable {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	*服务商ID.
	**/
	private Long supplierId;
	/**
	*姓名.
	**/
	private String name;
	/**
	*联系方式.
	**/
	private String phone;

}