package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_task_item")
public class TaskItem extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4175417576649826865L;

	/**
	*任务id.
	**/
	private Long taskId;
	/**
	*空间id.
	**/
	private Long spaceId;
	/**
	*名称.
	**/
	private String name;
	/**
	*内容.
	**/
	private String content;
}
