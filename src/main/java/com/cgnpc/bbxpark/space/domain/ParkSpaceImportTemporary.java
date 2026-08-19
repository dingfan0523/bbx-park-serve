package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@TableName("bbx_park_space_import_temporary")
@Data
public class ParkSpaceImportTemporary extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3545079290351631881L;
	/**
	*上级空间编码.
	**/
	private String parentSpaceCode;
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
	*批次.
	**/
	private String batchCode;
	/**
	*所属空间ID.
	**/
	private Long parentSpaceId;
	/**
	*排序序号.
	**/
	private String orderCode;
	/**
	*状态0默认1失败2成功.
	**/
	private String importStatus;
	/**
	*失败错误描述.
	**/
	private String importErrorDesc;
	/**
	*乐观锁.
	**/
	private String revision;
}
