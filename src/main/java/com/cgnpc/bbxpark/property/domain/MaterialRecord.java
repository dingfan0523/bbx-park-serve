package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 材料记录数据模型实体
 * @author huangyongtao
 * @date 2025/9/22 15:45
 */
@Data
@TableName("bbx_material_record")
public class MaterialRecord extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*入库单号.
	**/
	private String inboundNo;
	/**
	*记录类型;（1：入库；2：出库）.
	**/
	private Integer recordType;
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
	*当前库存.
	**/
	private Integer currentStock;
	/**
	*数据来源;（1：手动入库；2：工单维修）.
	**/
	private Integer dataSource;
	/**
	*备注.
	**/
	private String remark;
}
