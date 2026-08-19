
package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_space_image")
public class SpaceImage extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3613294439782273134L;
	/**
	*关联空间ID.
	**/
	private Long spaceId;
	/**
	*图片类型.
	**/
	private Integer type;
	/**
	*图片集合.
	**/
	private String images;
	/**
	*备注.
	**/
	private String remark;
}
