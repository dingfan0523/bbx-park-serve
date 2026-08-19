
package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_inspection_material")
public class InspectionMaterial extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4056563012008698830L;

	/**
	*巡检id.
	**/
	private Long inspectionId;
	/**
	*材料id.
	**/
	private Long materialId;
	/**
	*材料数量.
	**/
	private Integer materialNum;
}
