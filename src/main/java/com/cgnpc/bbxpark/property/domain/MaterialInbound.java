package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 材料入库数据模型实体
 * @author huangyongtao
 * @date 2025/9/22 15:44
 */
@Data
@TableName("bbx_material_inbound")
public class MaterialInbound extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*入库单号.
	**/
	private String inboundNo;
	/**
	*材料id.
	**/
	private Long materialId;
	/**
	*材料名称.
	**/
	private String materialName;
	/**
	*材料编码.
	**/
	private String materialCode;
	/**
	*材料类型;（1：器材；2：耗材）.
	**/
	private Integer materialType;
	/**
	*入库数量.
	**/
	private Integer quantity;
	/**
	*备注.
	**/
	private String remark;
}
