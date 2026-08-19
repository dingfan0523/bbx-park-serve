package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 维保材料数据模型实体
 * @author huangyongtao
 * @date 2025/10/16 15:14
 */
@Data
@TableName("bbx_maintain_material")
public class MaintainMaterial extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*维保id.
	**/
	private Long maintainId;
	/**
	*材料id.
	**/
	private Long materialId;
	/**
	*材料数量.
	**/
	private Integer materialNum;

}
