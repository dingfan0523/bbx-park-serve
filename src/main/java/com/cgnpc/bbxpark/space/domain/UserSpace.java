package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 空间用户关系实体类
 * @author huangyongtao
 * @date 2024/7/1 16:08
 */
@TableName("bbx_user_space")
@Data
public class UserSpace extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
	/**
	*用户ID.
	**/
	private String userId;
	/**
	*空间ID.
	**/
	private Long spaceId;

	/**
	*乐观锁.
	**/
	private String revision;

}
