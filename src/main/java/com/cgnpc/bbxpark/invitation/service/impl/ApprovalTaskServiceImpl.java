
package com.cgnpc.bbxpark.invitation.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.enums.ApprovalStatusEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.invitation.domain.ApprovalRecord;
import com.cgnpc.bbxpark.invitation.domain.ApprovalTask;
import com.cgnpc.bbxpark.invitation.domain.TaskAssignment;
import com.cgnpc.bbxpark.invitation.dto.model.ApprovalTaskListModel;
import com.cgnpc.bbxpark.invitation.dto.model.ApproverModel;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalActionParam;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalRecordPageParam;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalTaskParam;
import com.cgnpc.bbxpark.invitation.dto.param.ExtendContent;
import com.cgnpc.bbxpark.invitation.mapper.ApprovalTaskRepository;
import com.cgnpc.bbxpark.invitation.service.IApprovalRecordService;
import com.cgnpc.bbxpark.invitation.service.IApprovalTaskService;
import com.cgnpc.bbxpark.invitation.service.ITaskAssignmentService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.config.eventbus.ApprovalCompletedEvent;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 审批任务服务实现
 * @author 54766
 */
@Service("approvalTaskService")
public class ApprovalTaskServiceImpl extends ServiceImpl<ApprovalTaskRepository,ApprovalTask> implements IApprovalTaskService {
    /**
     * 注入repository.
     */
	@Resource
	private ITaskAssignmentService taskAssignmentService;
	@Resource
	private IApprovalRecordService approvalRecordService;
	@Autowired
	private ApprovalTaskRepository approvalTaskRepository;
	@Resource
	private AsyncEventBus asyncEventBus;
	@Resource
	private IUserApiService userApiService;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean createTask(ApprovalTaskParam param) {
		//校验参数
		AssertUtils.isFalse(param.getBusinessId() == null, "业务id不能为空");
		AssertUtils.isFalse(CollectionUtils.isEmpty(param.getTaskNodes()), "审批节点不能为空");
		param.getTaskNodes().forEach(item -> AssertUtils.isFalse(CollectionUtils.isEmpty(item.getUserIds()), "审批人信息不能为空"));

		//预先查出用户信息
		List<String> userIds = param.getTaskNodes().stream().filter(item -> !CollectionUtils.isEmpty(item.getUserIds()))
				.flatMap(item -> item.getUserIds().stream()).collect(Collectors.toList());
		List<UserInfoModel> users = userApiService.getByStaffNos(userIds);
		AssertUtils.isFalse(CollectionUtils.isEmpty(users), "未找到审批人信息");
		Map<String, UserInfoModel> userMap = users.stream().collect(Collectors.toMap(UserInfoModel::getId, Function.identity()));

		param.getTaskNodes().forEach(item -> {
			ApprovalTask task = new ApprovalTask();
			task.setBusinessId(param.getBusinessId());
			task.setStatus(ApprovalStatusEnum.WAITING.getCode());
			task.setExtendContent(JSONUtil.toJsonStr(item.getTaskExtend()));
			save(task);
			//保存审批人
			List<TaskAssignment> assignments = item.getUserIds().stream().map(userId -> {
				TaskAssignment assignment = new TaskAssignment();
				assignment.setBusinessId(param.getBusinessId());
				assignment.setTaskId(task.getId());
				assignment.setUserId(userId);
				if(userMap.containsKey(userId)){
					assignment.setUserName(userMap.get(userId).getUserName());
					assignment.setStaffid(userMap.get(userId).getStaffid());
				}
				return assignment;
			}).collect(Collectors.toList());
			taskAssignmentService.saveBatch(assignments);
		});
		return Boolean.TRUE;
	}

	@Override
	public IPage<Long> pendingApprovalPage(CudPageDto param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		String userId = WebFrameworkUtils.getHeaderUserId();
		IPage<Long> page = approvalTaskRepository.pendingApprovalPage(new Page<>(param.getCurrent(),
                param.getSize()),tenantId,userId);
		if (CollectionUtils.isEmpty(page.getRecords())) {
			return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
		}
		return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(), page.getRecords());
	}

	@Override
	public IPage<Long> approvedPage(CudPageDto param) {
		ApprovalRecordPageParam pageParam =  new ApprovalRecordPageParam();
		BeanUtils.copyProperties(param,pageParam);
		return approvalRecordService.page(pageParam);
	}

	@Override
	public List<Long> getApproveBusinessIds(List<Long> businessIds) {
		if(CollectionUtils.isEmpty(businessIds)){
			return Collections.emptyList();
		}
		String userId = WebFrameworkUtils.getHeaderUserId();
		List<TaskAssignment> assignments = taskAssignmentService.list(Wrappers.<TaskAssignment>lambdaQuery().eq(TaskAssignment::getUserId,userId).in(TaskAssignment::getBusinessId,businessIds));
		if(CollectionUtils.isEmpty(assignments)){
			return Collections.emptyList();
		}
		List<Long> taskIds = assignments.stream().map(TaskAssignment::getTaskId).collect(Collectors.toList());
		//查询当前用户待审批的任务信息
		List<ApprovalTask> tasks = list(Wrappers.<ApprovalTask>lambdaQuery().in(ApprovalTask::getId,taskIds).eq(ApprovalTask::getStatus,ApprovalStatusEnum.WAITING.getCode()).eq(ApprovalTask::getDeleted, Status.enabled.getKey()));
		if(CollectionUtils.isEmpty(tasks)){
			return Collections.emptyList();
		}
		return tasks.stream().map(ApprovalTask::getBusinessId).distinct().collect(Collectors.toList());
	}

	@Override
	public List<ApprovalTaskListModel> getApprovalTaskList(Long businessId) {
		if(businessId == null){
			return Collections.emptyList();
		}
		List<ApprovalTask> tasks = list(Wrappers.<ApprovalTask>lambdaQuery().eq(ApprovalTask::getBusinessId,businessId).eq(ApprovalTask::getDeleted, Status.enabled.getKey()));
		if(CollectionUtils.isEmpty(tasks)){
			return Collections.emptyList();
		}
		List<Long> taskIds = tasks.stream().map(ApprovalTask::getId).collect(Collectors.toList());
		List<TaskAssignment> assignmentList = taskAssignmentService.list(Wrappers.<TaskAssignment>lambdaQuery().in(TaskAssignment::getTaskId,taskIds));
		Map<Long, List<TaskAssignment>> assignmentMap = assignmentList.stream().collect(Collectors.groupingBy(TaskAssignment::getTaskId));
		return tasks.stream().map(task -> {
			ApprovalTaskListModel model = BeanUtils.convertTo(task, ApprovalTaskListModel::new);
			//审批人列表
			List<TaskAssignment> assignments = assignmentMap.getOrDefault(task.getId(), Collections.emptyList());
			model.setApproverList(BeanUtils.convertListTo(assignments, ApproverModel::new));
			return model;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized Boolean businessApprove(ApprovalActionParam param) {
		// 获取当前用户信息
		String userId = WebFrameworkUtils.getHeaderUserId();
		// 查找用户相关联的审批任务
		List<TaskAssignment> assignments = taskAssignmentService.list(Wrappers.<TaskAssignment>lambdaQuery().eq(TaskAssignment::getBusinessId, param.getBusinessId()).eq(TaskAssignment::getUserId, userId));
		AssertUtils.isFalse(CollectionUtils.isEmpty(assignments), "未找到待审批的任务");
		List<Long> allTaskIds = assignments.stream().map(TaskAssignment::getTaskId).collect(Collectors.toList());
		// 查找用户需要审批的任务
		List<ApprovalTask> tasks = this.list(Wrappers.<ApprovalTask>lambdaQuery().eq(ApprovalTask::getBusinessId, param.getBusinessId()).eq(ApprovalTask::getStatus, ApprovalStatusEnum.WAITING.getCode()).in(ApprovalTask::getId,allTaskIds));
		AssertUtils.isFalse(CollectionUtils.isEmpty(tasks), "未找到待审批的任务");
		List<Long> taskIds = tasks.stream().map(ApprovalTask::getId).collect(Collectors.toList());

		int targetStatus = param.getApproved() ? ApprovalStatusEnum.PASS.getCode() : ApprovalStatusEnum.REFUSE.getCode();
		List<ApprovalRecord> records = new ArrayList<>();
		tasks.forEach(task -> {
			task.setStatus(targetStatus);
			ApprovalRecord record = new ApprovalRecord();
			record.setTaskId(task.getId());
			record.setBusinessId(task.getBusinessId());
			record.setStatus(targetStatus);
			record.setUserId(userId);
			record.setStaffid(assignments.get(0).getStaffid());
			record.setUserName(assignments.get(0).getUserName());
			record.setRemark(param.getRemark());
			ExtendContent content = JSONUtil.toBean(task.getExtendContent(), ExtendContent.class);
			record.setExtend1(content.getDeviceName());
			records.add(record);
		});
		// 更新任务状态
		updateBatchById(tasks);
		//更新任务分配状态
		List<TaskAssignment> assignmentList = assignments.stream().filter(a -> taskIds.contains(a.getTaskId()))
				.peek(a-> a.setApproved(0)).collect(Collectors.toList());
		taskAssignmentService.updateBatchById(assignmentList);
		// 新增审批记录
		approvalRecordService.saveBatch(records);

		// 检查是否所有任务都已完成，如果是则通知业务模块
		checkAndNotifyBusiness(param.getBusinessId(),param.getApproved(),param.getRemark());
		return Boolean.TRUE;
	}

	@Override
	public synchronized Boolean cancelTask(List<Long> businessIds) {
		if(CollectionUtils.isEmpty(businessIds)){
			return false;
		}
		List<ApprovalTask> tasks = list(Wrappers.<ApprovalTask>lambdaQuery().in(ApprovalTask::getBusinessId, businessIds).eq(ApprovalTask::getStatus,ApprovalStatusEnum.WAITING.getCode()).eq(ApprovalTask::getDeleted, Status.enabled.getKey()));
		if(!CollectionUtils.isEmpty(tasks)){
			tasks.forEach(task -> task.setStatus(ApprovalStatusEnum.CANCEL.getCode()));
			updateBatchById(tasks);
		}
		return true;
	}

	/**
	 * 检查审批状态并发送事件通知业务侧
	 *
	 * @param businessId 业务ID
	 */
	private void checkAndNotifyBusiness(Long businessId,Boolean currentApproved,String remark) {
		if(!currentApproved){
			//审批不通过,直接发送审批通知事件
			sendApprovalEvent(businessId,false,remark);
			//取消该业务的其他待审批任务
			cancelTask(Collections.singletonList(businessId));
			return;
		}
		// 查询是否还有待审批的任务
		long pendingCount = this.count(Wrappers.<ApprovalTask>lambdaQuery().eq(ApprovalTask::getBusinessId, businessId).eq(ApprovalTask::getStatus, ApprovalStatusEnum.WAITING.getCode()));
		// 如果没有待审批的任务，说明所有任务都已完成
		if (pendingCount == 0) {
			sendApprovalEvent(businessId, true,remark);
		}
	}

	/**
	 * 发送审批结果事件
	 *
	 * @param businessId 业务ID
	 * @param approved 审批结果 true-通过 false-拒绝
	 * @param remark 审批意见
	 */
	private void sendApprovalEvent(Long businessId, boolean approved,String remark) {
		ApprovalCompletedEvent event = new ApprovalCompletedEvent();
		event.setBusinessId(businessId);
		event.setApproved(approved);
		event.setRemark(remark);
		asyncEventBus.post(event);
	}
//
//	/**
//	 * 根据审批任务标识获得审批任务详情信息.
//	 * @Param [id] 审批任务标识
//	 * @Return 审批任务详情信息
//	 */
//	@Override
//	public ApprovalTaskModel detail(Long id) {
//		ApprovalTask approvalTask = this.get(id);
//		AssertUtils.notNull(approvalTask, SystemResultCode.RESULT_DATA_NONE.message());
//		return BeanUtils.convertTo(approvalTask, ApprovalTaskModel::new);
//	}
//
//	/**
//	 * 获取审批任务列表(分页).
//	 * @Param param 审批任务查询条件
//	 * @Return 审批任务信息列表（分页）
//	 */
//	@Override
//	public IPage<ApprovalTaskModel> page(ApprovalTaskPageParam param) {
//		PaginationEntity<ApprovalTaskPageParam> pageEntity = new PaginationEntity(param);
//		return this.selectPagination(pageEntity).convert(ApprovalTaskModel.class);
//	}
//
//	/**
//	 * 获取审批任务列表.
//	 * @Param param 审批任务查询条件
//	 * @Return 审批任务信息列表
//	 */
//	@Override
//	@SneakyThrows
//	public List<ApprovalTaskModel> list(ApprovalTaskListParam param) {
//		ApprovalTask approvalTask = BeanUtils.convertTo(param, ApprovalTask::new);
//		return this.selectEntitys(approvalTask).convert(ApprovalTaskModel.class);
//	}
//
//	/**
//	 * 新增审批任务.
//	 * @Param param 审批任务信息
//	 * @Return 新增审批任务是否成功
//	 */
//	@Override
//	public Boolean add(ApprovalTaskParam param) {
//		ApprovalTask approvalTask = BeanUtils.convertTo(param, ApprovalTask::new);
//		approvalTask.setId(null);
//		return this.insert(approvalTask) > 0;
//	}
//
//	/**
//	 * 批量新增审批任务.
//	 * @Param params 审批任务信息列表
//	 * @Return 批量新增审批任务是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean addBatch(List<ApprovalTaskParam> params) {
//		List<ApprovalTask> approvalTasks = BeanUtils.convertListTo(params, ApprovalTask::new);
//		return this.insertBatch(approvalTasks) > 0;
//	}
//
//	/**
//	 * 删除审批任务.
//	 * @Param id 审批任务标识
//	 * @Return 删除审批任务是否成功
//	 */
//	@Override
//	public Boolean remove(Long id) {
//		ApprovalTask approvalTask = this.get(id);
//		AssertUtils.notNull(approvalTask, SystemResultCode.RESULT_DATA_NONE.message());
//		return this.delete(id) > 0;
//	}
//
//	/**
//	 * 批量删除审批任务.
//	 * @Param ids 审批任务标识列表
//	 * @Return 批量删除审批任务是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean removeBatch(List<Long> ids) {
//		return this.deleteBatch(ids) > 0;
//	}
//
//	/**
//	 * 编辑审批任务信息.
//	 * @Param param 审批任务信息
//	 * @Return 编辑审批任务是否成功
//	 */
//	@Override
//	public Boolean edit(Long id, ApprovalTaskParam param) {
//		ApprovalTask approvalTask = this.get(id);
//		AssertUtils.notNull(approvalTask, SystemResultCode.RESULT_DATA_NONE.message());
//		ApprovalTask editParam = BeanUtils.convertTo(param, ApprovalTask::new);
//		/**
//		 * 保护不可编辑字段
//		 */
//
//		return this.update(editParam) > 0;
//	}
//
//	/**
//	 * 批量编辑审批任务信息.
//	 * @Param params 审批任务信息列表
//	 * @Return 批量编辑审批任务是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean editBatch(List<ApprovalTaskParam> params) {
//		List<ApprovalTask> approvalTasks = BeanUtils.convertListTo(params, ApprovalTask::new);
//		return this.updateBatch(approvalTasks) > 0;
//	}
//
//	/**
//	 * 启用审批任务.
//	 * @Param id 审批任务标识
//	 * @Return 启用审批任务是否成功
//	 */
//	@Override
//	public Boolean enable(Long id) {
//		ApprovalTask approvalTask = new ApprovalTask();
//	 	approvalTask.setId(id);
//		approvalTask.setStatus((int) Status.enabled.getKey());
//		return this.update(approvalTask) > 0;
//	}
//
//	/**
//	 * 批量启用审批任务信息.
//	 * @Param ids 审批任务标识列表
//	 * @Return 批量启用审批任务是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean enableBatch(List<Long> ids) {
//		List<ApprovalTask> approvalTasks = ids.stream().map(id -> {
//			ApprovalTask approvalTask = new ApprovalTask();
//			approvalTask.setId(id);
//			approvalTask.setStatus((int) Status.enabled.getKey());
//			return approvalTask;
//		}).collect(Collectors.toList());
//		return this.updateBatch(approvalTasks) > 0;
//	}
//
//	/**
//	 * 禁用审批任务.
//	 * @Param id 审批任务标识
//	 * @Return 禁用审批任务是否成功
//	 */
//	@Override
//	public Boolean disable(Long id) {
//		ApprovalTask approvalTask = new ApprovalTask();
//		approvalTask.setId(id);
//		approvalTask.setStatus((int) Status.disabled.getKey());
//		return this.update(approvalTask) > 0;
//	}
//
//	/**
//	 * 批量禁用审批任务信息.
//	 * @Param ids 审批任务标识列表
//	 * @Return 批量禁用审批任务是否成功
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean disableBatch(List<Long> ids) {
//		List<ApprovalTask> approvalTasks = ids.stream().map(id -> {
//			ApprovalTask approvalTask = new ApprovalTask();
//			approvalTask.setId(id);
//			approvalTask.setStatus((int) Status.disabled.getKey());
//			return approvalTask;
//		}).collect(Collectors.toList());
//		return this.updateBatch(approvalTasks) > 0;
//	}
}
