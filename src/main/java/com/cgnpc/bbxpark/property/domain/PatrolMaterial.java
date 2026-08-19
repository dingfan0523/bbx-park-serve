
package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_patrol_material")
public class PatrolMaterial extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4684745613887002501L;

	/**
	*巡更id.
	**/
	private Long patrolId;
	/**
	*材料id.
	**/
	private Long materialId;
	/**
	*材料数量.
	**/
	private Integer materialNum;
}
