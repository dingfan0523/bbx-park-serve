
package com.cgnpc.bbxpark.invitation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.invitation.domain.TaskAssignment;
import com.cgnpc.bbxpark.invitation.mapper.TaskAssignmentRepository;
import com.cgnpc.bbxpark.invitation.service.ITaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("taskAssignmentService")
public class TaskAssignmentServiceImpl extends ServiceImpl<TaskAssignmentRepository,TaskAssignment> implements ITaskAssignmentService {
    /**
     * 注入repository.
     */
	@Autowired
	private TaskAssignmentRepository taskAssignmentRepository;




//	/**
//	 * 根据审批分配人标识获得审批分配人详情信息.
//	 * @Param [id] 审批分配人标识
//	 * @Return 审批分配人详情信息
//	 */
//	@Override
//	public TaskAssignmentModel detail(Long id) {
//		TaskAssignment taskAssignment = this.get(id);
//		AssertUtils.notNull(taskAssignment, SystemResultCode.RESULT_DATA_NONE.message());
//		return BeanUtils.convertTo(taskAssignment, TaskAssignmentModel::new);
//	}
//
//	/**
//	 * 获取审批分配人列表(分页).
//	 * @Param param 审批分配人查询条件
//	 * @Return 审批分配人信息列表（分页）
//	 */
//	@Override
//	public IPage<TaskAssignmentModel> page(TaskAssignmentPageParam param) {
//		PaginationEntity<TaskAssignmentPageParam> pageEntity = new PaginationEntity(param);
//		return this.selectPagination(pageEntity).convert(TaskAssignmentModel.class);
//	}
//
//	/**
//	 * 获取审批分配人列表.
//	 * @Param param 审批分配人查询条件
//	 * @Return 审批分配人信息列表
//	 */
//	@Override
//	@SneakyThrows
//	public List<TaskAssignmentModel> list(TaskAssignmentListParam param) {
//		TaskAssignment taskAssignment = BeanUtils.convertTo(param, TaskAssignment::new);
//		return this.selectEntitys(taskAssignment).convert(TaskAssignmentModel.class);
//	}
//
//	/**
//	 * 新增审批分配人.
//	 * @Param param 审批分配人信息
//	 * @Return 新增审批分配人是否成功
//	 */
//	@Override
//	public Boolean add(TaskAssignmentParam param) {
//		TaskAssignment taskAssignment = BeanUtils.convertTo(param, TaskAssignment::new);
//		taskAssignment.setId(null);
//		return this.insert(taskAssignment) > 0;
//	}
//
//	/**
//	 * 批量新增审批分配人.
//	 * @Param params 审批分配人信息列表
//	 * @Return 批量新增审批分配人是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean addBatch(List<TaskAssignmentParam> params) {
//		List<TaskAssignment> taskAssignments = BeanUtils.convertListTo(params, TaskAssignment::new);
//		return this.insertBatch(taskAssignments) > 0;
//	}
//
//	/**
//	 * 删除审批分配人.
//	 * @Param id 审批分配人标识
//	 * @Return 删除审批分配人是否成功
//	 */
//	@Override
//	public Boolean remove(Long id) {
//		TaskAssignment taskAssignment = this.get(id);
//		AssertUtils.notNull(taskAssignment, SystemResultCode.RESULT_DATA_NONE.message());
//		return this.delete(id) > 0;
//	}
//
//	/**
//	 * 批量删除审批分配人.
//	 * @Param ids 审批分配人标识列表
//	 * @Return 批量删除审批分配人是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean removeBatch(List<Long> ids) {
//		return this.deleteBatch(ids) > 0;
//	}
//
//	/**
//	 * 编辑审批分配人信息.
//	 * @Param param 审批分配人信息
//	 * @Return 编辑审批分配人是否成功
//	 */
//	@Override
//	public Boolean edit(Long id, TaskAssignmentParam param) {
//		TaskAssignment taskAssignment = this.get(id);
//		AssertUtils.notNull(taskAssignment, SystemResultCode.RESULT_DATA_NONE.message());
//		TaskAssignment editParam = BeanUtils.convertTo(param, TaskAssignment::new);
//		/**
//		 * 保护不可编辑字段
//		 */
//
//		return this.update(editParam) > 0;
//	}
//
//	/**
//	 * 批量编辑审批分配人信息.
//	 * @Param params 审批分配人信息列表
//	 * @Return 批量编辑审批分配人是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean editBatch(List<TaskAssignmentParam> params) {
//		List<TaskAssignment> taskAssignments = BeanUtils.convertListTo(params, TaskAssignment::new);
//		return this.updateBatch(taskAssignments) > 0;
//	}
//
//	/**
//	 * 启用审批分配人.
//	 * @Param id 审批分配人标识
//	 * @Return 启用审批分配人是否成功
//	 */
//	@Override
//	public Boolean enable(Long id) {
//		TaskAssignment taskAssignment = new TaskAssignment();
//	 	taskAssignment.setId(id);
//		return this.update(taskAssignment) > 0;
//	}
//
//	/**
//	 * 批量启用审批分配人信息.
//	 * @Param ids 审批分配人标识列表
//	 * @Return 批量启用审批分配人是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean enableBatch(List<Long> ids) {
//		List<TaskAssignment> taskAssignments = ids.stream().map(id -> {
//			TaskAssignment taskAssignment = new TaskAssignment();
//			taskAssignment.setId(id);
//			return taskAssignment;
//		}).collect(Collectors.toList());
//		return this.updateBatch(taskAssignments) > 0;
//	}
//
//	/**
//	 * 禁用审批分配人.
//	 * @Param id 审批分配人标识
//	 * @Return 禁用审批分配人是否成功
//	 */
//	@Override
//	public Boolean disable(Long id) {
//		TaskAssignment taskAssignment = new TaskAssignment();
//		taskAssignment.setId(id);
//		return this.update(taskAssignment) > 0;
//	}
//
//	/**
//	 * 批量禁用审批分配人信息.
//	 * @Param ids 审批分配人标识列表
//	 * @Return 批量禁用审批分配人是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean disableBatch(List<Long> ids) {
//		List<TaskAssignment> taskAssignments = ids.stream().map(id -> {
//			TaskAssignment taskAssignment = new TaskAssignment();
//			taskAssignment.setId(id);
//			return taskAssignment;
//		}).collect(Collectors.toList());
//		return this.updateBatch(taskAssignments) > 0;
//	}
}
