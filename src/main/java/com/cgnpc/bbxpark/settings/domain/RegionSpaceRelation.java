
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 区域管理员空间关联数据模型实体
 * @author huangyongtao
 * @date 2025/3/11 11:09
 */
@Data
@TableName("bbx_region_space_relation")
public class RegionSpaceRelation extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*区域管理id.
	**/
	private Long regionId;
	/**
	*空间id.
	**/
	private Long spaceId;

}
