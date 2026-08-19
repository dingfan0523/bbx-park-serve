package com.cgnpc.bbxpark.property.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.property.domain.TaskItem;
import com.cgnpc.bbxpark.property.dto.param.TaskItemParam;
import com.cgnpc.bbxpark.property.mapper.TaskItemRepository;
import com.cgnpc.bbxpark.property.service.ITaskItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务明细服务实现
 */
@Slf4j
@Service
public class TaskItemServiceImpl extends ServiceImpl<TaskItemRepository, TaskItem> implements ITaskItemService {

	/**
	 * 批量新增任务明细.
	 * @Param params 任务明细信息列表
	 * @Return 批量新增任务明细是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<TaskItemParam> params) {
		List<TaskItem> taskItems = BeanUtils.convertListTo(params, TaskItem::new);
		return saveBatch(taskItems);
	}

	/**
	 * 删除任务明细.
	 * @Param taskId 任务标识
	 * @Return 删除任务明细是否成功
	 */
	@Override
	public Boolean remove(Long taskId) {
		return remove(Wrappers.<TaskItem>lambdaQuery().eq(TaskItem::getTaskId, taskId));
	}
}
