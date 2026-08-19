
package com.cgnpc.bbxpark.workorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.workorder.domain.WorkTaskItem;
import com.cgnpc.bbxpark.workorder.dto.model.WorkTaskItemModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskItemParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkTaskItemRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkTaskItemService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * @Description 工单任务项服务实现
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
@Service("workTaskItemService")
public class WorkTaskItemServiceImpl extends ServiceImpl<WorkTaskItemRepository, WorkTaskItem> implements IWorkTaskItemService {


	/**
	 * 获取工单任务项列表.
	 * @Param workId工单标识
	 * @Return 工单任务项信息列表
	 */
	@Override
	@SneakyThrows
	public List<WorkTaskItemModel> list(Long workId) {
		List<WorkTaskItem> workTaskItems = this.list(buildQuery(workId));
		return BeanUtils.convertListTo(workTaskItems, WorkTaskItemModel::new);
	}

	/**
	 * 新增工单任务项.
	 * @Param param 工单任务项信息
	 * @Return 新增工单任务项是否成功
	 */
	@Override
	public Boolean add(WorkTaskItemParam param) {
		WorkTaskItem workTaskItem = BeanUtils.convertTo(param, WorkTaskItem::new);
		workTaskItem.setId(null);
		return this.save(workTaskItem);
	}

	/**
	 * 批量新增工单任务项.
	 * @Param params 工单任务项信息列表
	 * @Return 批量新增工单任务项是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<WorkTaskItemParam> params) {
		if (CollectionUtil.isEmpty(params)) {
			return true;
		}
		List<WorkTaskItem> workTaskItems = BeanUtils.convertListTo(params, WorkTaskItem::new);
		return this.saveBatch(workTaskItems);
	}

	/**
	 * 删除工单任务项.
	 * @Param id 工单任务项标识
	 * @Return 删除工单任务项是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		return this.remove(Wrappers.<WorkTaskItem>lambdaQuery().eq(WorkTaskItem::getId, id));
	}

	private LambdaQueryWrapper<WorkTaskItem> buildQuery(Long workId) {
		LambdaQueryWrapper<WorkTaskItem> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(workId), WorkTaskItem::getWorkId, workId);
		query.orderByAsc(WorkTaskItem::getTaskGroup);
		return query;
	}
}
