
package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@TableName("bbx_space_import_batch")
@Data
public class SpaceImportBatch extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4595287791332957672L;


	/**
	*批次.
	**/
	private String batchCode;
	/**
	*导入总数.
	**/
	private Integer importAllNum;
	/**
	*导入成功数.
	**/
	private Integer importSuccessNum;
	/**
	*导入失败数.
	**/
	private Integer importErrorNum;
	/**
	*乐观锁.
	**/
	private String revision;
}
