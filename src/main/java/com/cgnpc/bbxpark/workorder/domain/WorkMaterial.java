
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 工单材料数据模型实体
 * @author huangyongtao
 * @date 2025/11/4 16:20
 */
@Data
@TableName("bbx_work_material")
public class WorkMaterial extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*工单id.
	**/
	private Long workId;
	/**
	*材料id.
	**/
	private Long materialId;
	/**
	*材料名称.
	**/
	private String materialName;
	/**
	*材料编码.
	**/
	private String materialCode;
	/**
	*材料类型;（1：器材；2：耗材）.
	**/
	private Integer materialType;
	/**
	*材料数量.
	**/
	private Integer materialNum;
	/**
	 *材料使用数量.
	 **/
	private Integer materialUseNum;
}