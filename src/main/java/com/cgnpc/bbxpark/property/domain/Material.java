package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 材料数据模型实体
 * @author huangyongtao
 * @date 2025/9/22 15:42
 */
@Data
@TableName("bbx_material")
public class Material extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * 存放位置id
     */
    private Long spaceId;
	/**
	*材料名称.
	**/
	private String materialName;
	/**
	*材料编码.
	**/
	private String materialCode;
	/**
	*材料类型;（1：器材；2：耗材-备品；3：耗材-备件）.
	**/
	private Integer materialType;
	/**
	*库存数量.
	**/
	private Integer stockQuantity;
	/**
	*库存预警值.
	**/
	private Integer stockWarning;
	/**
	*库存状态;（1：库存充足；2：库存不足；3：缺货）.
	**/
	private Integer stockStatus;
	/**
	*备注.
	**/
    @TableField(strategy = FieldStrategy.IGNORED)
	private String remark;

    /**
     *原始库存.
     **/
    private Integer originalStock;
    /**
     *入库次数.
     **/
    private Integer inbound;
    /**
     *入库数量
     **/
    private Integer inboundQuantity;
    /**
     *出库次数.
     **/
    private Integer outbound;
    /**
     *出库数量
     **/
    private Integer outboundQuantity;
}
