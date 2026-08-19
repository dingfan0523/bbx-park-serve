
package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("bbx_park_space")
public class ParkSpace extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4203143109627834696L;

	/**
	*空间编码.
	**/
	private String spaceCode;
	/**
	*空间名称.
	**/
	private String spaceName;
	/**
	*空间地址.
	**/
	private String spaceAddr;
	/**
	*空间描述.
	**/
	private String spaceDesc;
	/**
	*所属空间ID.
	**/
	private Long parentSpaceId;
	/**
	 *所属空间编码.
	 **/
	private String parentSpaceCode;
	/**
	*排序序号.
	**/
	private Integer orderCode;
	/**
	*状态0在用1删除.
	**/
	private Integer spaceStatus;
	/**
	*乐观锁.
	**/
	private String revision;

	/**
	 *所属楼层物模型编码.
	 **/
	private String sslcCode;
}
