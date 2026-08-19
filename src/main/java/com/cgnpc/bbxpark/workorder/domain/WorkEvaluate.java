
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 工单评价数据模型实体
 * @author huangyongtao
 * @date 2025/11/4 16:33
 */
@Data
@TableName("bbx_work_evaluate")
public class WorkEvaluate extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*工单id.
	**/
	private Long workId;
	/**
	*分数.
	**/
	private Integer score;
	/**
	*内容.
	**/
	private String content;
    /**
     *操作人id.
     **/
    private String operateUid;
	/**
	*操作人名称.
	**/
	private String operateUname;
	/**
	*操作人工号.
	**/
	private String operateStaffid;
}