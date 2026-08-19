package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.TaskItem;
import com.cgnpc.bbxpark.property.dto.param.TaskItemParam;

import java.util.List;

/**
 * 任务明细服务接口
 */
public interface ITaskItemService extends IService<TaskItem> {
	/**
	 * 批量新增任务明细.
	 * @Param params 任务明细信息列表
	 * @Return 批量新增任务明细是否成功
	 */
	Boolean addBatch(List<TaskItemParam> params);

	/**
	 * 删除任务明细.
	 * @Param taskId 任务标识
	 * @Return 删除任务明细是否成功
	 */
	Boolean remove(Long taskId);
}
