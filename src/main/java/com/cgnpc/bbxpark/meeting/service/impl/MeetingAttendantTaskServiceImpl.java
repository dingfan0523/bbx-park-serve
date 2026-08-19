
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.common.constant.MeetingConstant;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.*;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantTaskDetailRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantTaskRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveRepository;
import com.cgnpc.bbxpark.meeting.service.*;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.config.eventbus.AttendantTaskEvent;
import com.google.common.eventbus.AsyncEventBus;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 会服人员任务服务实现
 * @author huangyongtao
 * @date 2024/12/23 17:20
 */
@Service("meetingAttendantTaskService")
public class MeetingAttendantTaskServiceImpl extends ServiceImpl<MeetingAttendantTaskRepository, MeetingAttendantTask> implements IMeetingAttendantTaskService {

	@Autowired
	private IUserApiService userApiService;

	@Autowired
	private IMeetingAttendantTaskDetailService meetingAttendantTaskDetailService;

    @Autowired
    private MeetingAttendantTaskDetailRepository meetingAttendantTaskDetailRepository;

	@Autowired
	private IMeetingServiceService meetingServiceService;

	@Autowired
	private IMeetingRoomServiceService meetingRoomServiceService;

	@Autowired
	private IMeetingReserveSeatService meetingReserveSeatService;

	@Autowired
	MeetingReserveRepository meetingReserveRepository;

	@Autowired
	private IMeetingReserveFileService meetingReserveFileService;

	@Autowired
	private IFileService fileService;

	@Resource
	private AsyncEventBus asyncEventBus;

	@Autowired
	private IMeetingAttendantRoomService meetingAttendantRoomService;

	@Autowired
	private IMeetingAttendantEvaluateService meetingAttendantEvaluateService;
	@Autowired
	private IMessageCommonService messageCommonService;


	/**
	 * 根据会服人员任务标识获得会服人员任务详情信息.
	 * @Param [id] 会服人员任务标识
	 * @Return 会服人员任务详情信息
	 */
	@Override
	public MeetingAttendantTaskModel detail(MeetingAttendantTaskParam param) {
		MeetingAttendantTask meetingAttendantTask = this.getById(param.getId());
		AssertUtils.notNull(meetingAttendantTask, SystemResultCode.RESULT_DATA_NONE.message());
		MeetingAttendantTaskDetailListParam detailParam = new MeetingAttendantTaskDetailListParam();
		detailParam.setTaskId(param.getId());
		List<MeetingAttendantTaskDetailModel> detailModels = meetingAttendantTaskDetailService.list(detailParam);
		MeetingAttendantTaskModel model = BeanUtils.convertTo(meetingAttendantTask, MeetingAttendantTaskModel::new);
		model.setTaskDetailModelList(detailModels);
		if(MeetingAttendantTaskTypeEnum.IN.getValue().equals(meetingAttendantTask.getServiceType())){
			model.setUpdateTime(detailModels.get(detailModels.size()-1).getCreateTime());
		}
		model.setFileModelList(fileService.findByTypeAndRelatedId(FileTypeEnum.ATTENDANT.getValue(), meetingAttendantTask.getId()));
		return model;
	}

	@Override
	public MeetingAttendantTaskModel simpleDetail(Long id) {
		MeetingAttendantTask meetingAttendantTask = this.getById(id);
		AssertUtils.notNull(meetingAttendantTask, SystemResultCode.RESULT_DATA_NONE.message());
		MeetingAttendantTaskDetailListParam detailParam = new MeetingAttendantTaskDetailListParam();
		detailParam.setTaskId(id);
		List<MeetingAttendantTaskDetailModel> detailModels = meetingAttendantTaskDetailService.list(detailParam);
		MeetingAttendantTaskModel model = BeanUtils.convertTo(meetingAttendantTask, MeetingAttendantTaskModel::new);
		model.setTaskDetailModelList(detailModels);
		return model;
	}

	@Override
	public IPage<MeetingAttendantTaskModel> page(MeetingAttendantTaskPageParam param) {
		//租户隔离
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		param.setUserId(WebFrameworkUtils.getHeaderUserId());

		IPage<MeetingAttendantTask> page = page(new Page<>(param.getCurrent(), param.getSize()),Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getTenantId, param.getTenantId()).eq(MeetingAttendantTask::getReserveId, param.getReserveId()));
		if (CollectionUtil.isEmpty(page.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(), BeanUtils.convertListTo(page.getRecords(), MeetingAttendantTaskModel::new));
	}

	/**
	 * 获取会服人员任务列表(分页).
	 * @Param param 会服人员任务查询条件
	 * @Return 会服人员任务信息列表（分页）
	 */
	@Override
	public IPage<MeetingAttendantTaskPageModel> pageApp(MeetingAttendantTaskPageParam param) {
		//租户隔离
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		param.setUserId(WebFrameworkUtils.getHeaderUserId());

		IPage<MeetingAttendantTaskPageModel> page = getBaseMapper().pageApp(new Page<>(param.getCurrent(), param.getSize()), param);
		List<MeetingAttendantTaskPageModel> list = page.getRecords();
		if (CollectionUtil.isEmpty(list)) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		list.forEach(this::handlePageApp);
		return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(), list);
	}

	private void handlePageApp(MeetingAttendantTaskPageModel model){
		switch (model.getServiceType()){
			case 1:
			case 2:
				List<MeetingAttendantTaskDetail> details = meetingAttendantTaskDetailService.findByTaskId(model.getId());
				List<String> names = details.stream().map(MeetingAttendantTaskDetail::getSubmitUname).collect(Collectors.toList());
				model.setCallName(String.join(",",names));
				model.setCallTime(details.get(details.size()-1).getCreateTime());
				model.setCallDuration(handleDuration(model.getCallTime(), new Date()));
				break;
			case 3:
				MeetingReserve reserve = meetingReserveRepository.selectOne(Wrappers.<MeetingReserve>lambdaQuery().eq(MeetingReserve::getRoomId, model.getRoomId())
						.gt(MeetingReserve::getStartTime,new Date())
						.eq(MeetingReserve::getRoomId, model.getRoomId())
						.eq(MeetingReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId())
						.eq(MeetingReserve::getStatus, MeetingReserveStatusEnum.START.getCode())
						.eq(MeetingReserve::getDraft,  Status.disabled.getKey())
						.eq(MeetingReserve::getCancelFlag, Status.disabled.getKey())
						.orderByAsc(MeetingReserve::getStartTime).last("limit 1"));
				if(ObjectUtil.isEmpty(reserve)){
					model.setReserveName(null);
					model.setStartTime(null);
				}else{
					model.setReserveName(reserve.getReserveName());
					model.setStartTime(reserve.getStartTime());
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 获取会服人员任务列表.
	 * @Param param 会服人员任务查询条件
	 * @Return 会服人员任务信息列表
	 */
	@Override
	@SneakyThrows
	public List<MeetingAttendantTaskModel> list(MeetingAttendantTaskListParam param) {
		MeetingAttendantTaskPageParam pageParam = BeanUtils.convertTo(param, MeetingAttendantTaskPageParam::new);
		List<MeetingAttendantTask> list = this.list(buildQuery(pageParam));
		return CollectionUtil.isEmpty(list) ? new ArrayList<>(): BeanUtils.convertListTo(list, MeetingAttendantTaskModel::new);
	}

	private LambdaQueryWrapper<MeetingAttendantTask> buildQuery(MeetingAttendantTaskPageParam param) {
		LambdaQueryWrapper<MeetingAttendantTask> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MeetingAttendantTask::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.eq(ObjectUtil.isNotEmpty(param.getReserveId()), MeetingAttendantTask::getReserveId, param.getReserveId());
		query.eq(ObjectUtil.isNotEmpty(param.getServiceType()), MeetingAttendantTask::getServiceType, param.getServiceType());
		query.orderByAsc(MeetingAttendantTask::getCreateTime);
		return query;
	}

	/**
	 * 新增会服人员任务.
	 * @Param param 会服人员任务信息
	 * @Return 新增会服人员任务是否成功
	 */
	@Override
	public Boolean add(Long reserveId, MeetingReserveAppSaveParam param) {
		param.setId(reserveId);
		long filePintCount = CollectionUtil.isEmpty(param.getFileParamList()) ? 0L : param.getFileParamList().stream().filter(item -> Integer.valueOf(Status.enabled.getKey()).equals(item.getPrinting())).count();
		if(MeetingReserveTypeEnum.ORDINARY.getValue().equals(param.getMeetingType())
				&& CollectionUtil.isEmpty(param.getServiceParamList()) && CollectionUtil.isEmpty(param.getSeatParamList()) && filePintCount <= 0L){
			return true;
		}
		MeetingAttendantTask task = handleTask(BeanUtils.convertTo(param, MeetingReserve::new), MeetingAttendantTaskTypeEnum.BEFORE.getValue());
		List<MeetingAttendantTaskDetail> taskDetails = new ArrayList<>();
		UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
		if(MeetingReserveTypeEnum.VIDEO.getValue().equals(param.getMeetingType())){
			handleTaskDetail(null,user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.VIDEO.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.VIDEO.getValue(), taskDetails);
		}
		if(Status.enabled.getKey().equals(param.getRetainedAudio())){
			handleTaskDetail(null, user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.RECORDING.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.RECORDING.getValue(), taskDetails);
		}
		if(filePintCount > 0L){
			handleTaskDetail(null, user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.PRINT.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.PRINT.getValue(), taskDetails);
		}
		if(CollectionUtil.isNotEmpty(param.getSeatParamList())){
			handleTaskDetail(null, user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.SEAT.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.SEAT.getValue(), taskDetails);
		}
		if(CollectionUtil.isNotEmpty(param.getServiceParamList())){
			param.getServiceParamList().forEach(service -> {
				handleTaskDetail(service, user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.ORDINARY.getName(), MeetingAttendantTaskAttributeEnum.ORDINARY.getValue(), MeetingAttendantTaskAttributeTypeEnum.ORDINARY.getValue(), taskDetails);
			});
		}
		if(CollectionUtil.isNotEmpty(taskDetails)){
			meetingAttendantTaskDetailService.addBatch(taskDetails);
		}
		return true;
	}

	/**
	 * 批量新增会服人员任务.
	 * @Param params 会服人员任务信息列表
	 * @Return 批量新增会服人员任务是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<MeetingAttendantTaskParam> params) {
		List<MeetingAttendantTask> meetingAttendantTasks = BeanUtils.convertListTo(params, MeetingAttendantTask::new);
		return this.saveBatch(meetingAttendantTasks);
	}

	private void handleTaskDetail(MeetingServiceDetailParam service, UserInfoModel user, long taskId, String serviceName, int serviceAttribute, int attributeType, List<MeetingAttendantTaskDetail> taskDetails){
		MeetingAttendantTaskDetail detail = new MeetingAttendantTaskDetail();
		detail.setTaskId(taskId);
		if(ObjectUtil.isEmpty(user)){
			detail.setSubmitUname("系统");
		}else{
			detail.setSubmitUid(user.getId());
			detail.setSubmitUname(user.getUserName());
			detail.setSubmitStaffid(user.getStaffid());
		}
		detail.setServiceName(serviceName);
		detail.setServiceAttribute(serviceAttribute);
		detail.setAttributeType(attributeType);
		if(service != null){
			detail.setServiceId(service.getId());
			detail.setServiceName(service.getName());
			detail.setServiceStandard(service.getStandard());
			detail.setServiceWarn(service.getWarn());
			detail.setServiceInstructions(service.getInstructions());
		}
		taskDetails.add(detail);
	}

	private MeetingAttendantTaskDetail handleTaskDetail(MeetingServiceDetailParam service, UserInfoModel user, long taskId, long tenantId, String serviceName, int serviceAttribute, int attributeType){
		MeetingAttendantTaskDetail detail = new MeetingAttendantTaskDetail();
		detail.setTaskId(taskId);
		if(ObjectUtil.isEmpty(user)){
			detail.setSubmitUname("系统");
		}else{
			detail.setSubmitUid(user.getId());
			detail.setSubmitUname(user.getUserName());
			detail.setSubmitStaffid(user.getStaffid());
		}
		detail.setServiceName(serviceName);
		detail.setServiceAttribute(serviceAttribute);
		detail.setAttributeType(attributeType);
		if(service != null){
			detail.setServiceId(service.getId());
			detail.setServiceName(service.getName());
			detail.setServiceStandard(service.getStandard());
			detail.setServiceWarn(service.getWarn());
			detail.setServiceInstructions(service.getInstructions());
		}
		detail.setTenantId(tenantId);
		return detail;
	}



	/**
	 * 编辑会服人员任务信息.
	 * @Param param 会服人员任务信息
	 * @Return 编辑会服人员任务是否成功
	 */
	@Override
	public Boolean edit(MeetingAttendantTaskParam param) {
		MeetingAttendantTask meetingAttendantTask = this.getById(param.getId());
		AssertUtils.notNull(meetingAttendantTask, SystemResultCode.RESULT_DATA_NONE.message());
		MeetingAttendantTask editParam = BeanUtils.convertTo(param, MeetingAttendantTask::new);
		return this.updateById(editParam);
	}

	/**
	 * 批量编辑会服人员任务信息.
	 * @Param params 会服人员任务信息列表
	 * @Return 批量编辑会服人员任务是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean editBatch(List<MeetingAttendantTaskParam> params) {
		List<MeetingAttendantTask> meetingAttendantTasks = BeanUtils.convertListTo(params, MeetingAttendantTask::new);
		return this.updateBatchById(meetingAttendantTasks);
	}

	@Override
	public List<MeetingServiceModel> serviceDetail(MeetingAttendantTaskParam param) {
		List<MeetingRoomService> roomServices = meetingRoomServiceService.list(Wrappers.<MeetingRoomService>lambdaQuery().select(MeetingRoomService::getServiceId).eq(MeetingRoomService::getRoomId, param.getRoomId()));
		List<Long> serviceIds = CollectionUtil.isEmpty(roomServices) ? new ArrayList<>() : roomServices.stream().map(MeetingRoomService::getServiceId).collect(Collectors.toList());
		List<MeetingService> services = CollectionUtil.isEmpty(serviceIds) ? new ArrayList<>() : meetingServiceService.list(Wrappers.<MeetingService>lambdaQuery().in(MeetingService::getId, serviceIds));
		List<MeetingServiceModel> serviceModels = BeanUtils.convertListTo(services, MeetingServiceModel::new);
		List<Long> userServiceIds = new ArrayList<>();
		if(ObjectUtil.isNotEmpty(param.getReserveId())){
			List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().select(MeetingAttendantTask::getId).eq(MeetingAttendantTask::getReserveId, param.getReserveId()).eq(MeetingAttendantTask::getServiceType, MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
			Long taskId = CollectionUtil.isEmpty(tasks) ? 0L : tasks.get(0).getId();
			List<MeetingAttendantTaskDetail> details = meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, taskId).eq(MeetingAttendantTaskDetail::getServiceAttribute, MeetingAttendantTaskAttributeTypeEnum.ORDINARY.getValue()));
			userServiceIds = CollectionUtil.isEmpty(details) ? new ArrayList<>() : details.stream().map(MeetingAttendantTaskDetail::getServiceId).collect(Collectors.toList());

			List<Long> different = userServiceIds.stream().filter(item -> !serviceIds.contains(item)).collect(Collectors.toList());
			details.stream().filter(p-> different.contains(p.getServiceId())).forEach(detail -> {
				MeetingServiceModel serviceModel = new MeetingServiceModel();
				serviceModel.setId(detail.getServiceId());
				serviceModel.setName(detail.getServiceName());
				serviceModel.setStandard(detail.getServiceStandard());
				serviceModel.setInstructions(detail.getServiceInstructions());
				serviceModel.setWarn(detail.getServiceWarn());
				serviceModel.setSelected(Status.enabled.getKey());
				serviceModel.setValid(Status.disabled.getKey());
				serviceModels.add(serviceModel);
			});
		}
		if(CollectionUtil.isNotEmpty(serviceIds)){
			List<Long> finalUserServiceIds = userServiceIds;
			serviceModels.forEach(serviceModel -> {
				if(finalUserServiceIds.contains(serviceModel.getId()) || (ObjectUtil.isEmpty(param.getReserveId()) && Status.enabled.getKey().equals(serviceModel.getCommon()))){
					serviceModel.setSelected(Status.enabled.getKey());
				}else{
					serviceModel.setSelected(Status.disabled.getKey());
				}
			});
		}
		return serviceModels;
	}

	@Override
	public Boolean removeByReserveId(Long reserveId) {
		List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getReserveId, reserveId));
		if(CollectionUtil.isNotEmpty(tasks)){
			List<Long> taskIds = tasks.stream().map(MeetingAttendantTask::getId).collect(Collectors.toList());
			this.baseMapper.deleteBatchIds(taskIds);
			meetingAttendantTaskDetailService.removeByTaskIds(taskIds);
		}
		return true;
	}

	@Override
	public Boolean checkService(MeetingReserveAppSaveParam param) {
		if(CollectionUtil.isEmpty(param.getServiceParamList())){
			return true;
		}
		List<Long> serviceIdList = param.getServiceParamList().stream().map(MeetingServiceDetailParam::getId).collect(Collectors.toList());
		List<MeetingRoomService> roomServices = meetingRoomServiceService.list(Wrappers.<MeetingRoomService>lambdaQuery().select(MeetingRoomService::getServiceId).eq(MeetingRoomService::getRoomId, param.getRoomId()));
		AssertUtils.notNull(roomServices, "会服不可用");
		List<Long> serviceIds = roomServices.stream().map(MeetingRoomService::getServiceId).collect(Collectors.toList());
		List<Long> different = serviceIdList.stream().filter(item -> !serviceIds.contains(item)).collect(Collectors.toList());
		AssertUtils.isTrue(CollectionUtil.isEmpty(different), "会服不可用");
		return true;
	}

	@Override
	public synchronized Boolean editServiceBeforeRecording(MeetingReserveAppSaveParam param) {
		MeetingReserve reserve = meetingReserveRepository.selectById(param.getId());
		reserveCheck(reserve);
		//状态检查
		AssertUtils.isTrue(MeetingReserveStatusEnum.START.getCode().equals(reserve.getStatus()), "会议状态已变更，无法更改音频文件");
		List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getReserveId, param.getId()).eq(MeetingAttendantTask::getServiceType, MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
		List<MeetingAttendantTaskDetail> taskDetails = CollectionUtil.isEmpty(tasks) ? new ArrayList<>() : meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId()).eq(MeetingAttendantTaskDetail::getServiceAttribute, MeetingAttendantTaskAttributeTypeEnum.RECORDING.getValue()));

		checkTaskService(tasks);
		UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
		if(Status.enabled.getKey().equals(param.getRetainedAudio())){
			MeetingAttendantTask task = handleTask(reserve, tasks);
			if(CollectionUtil.isEmpty(taskDetails)){
				List<MeetingAttendantTaskDetail> details = new ArrayList<>();
				handleTaskDetail(null, user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.RECORDING.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.RECORDING.getValue(), details);
				meetingAttendantTaskDetailService.addBatch(taskDetails);
			}
		}else{
			if(CollectionUtil.isNotEmpty(taskDetails)){
				meetingAttendantTaskDetailService.removeById(taskDetails.get(0).getId());
			}
		}
		MeetingReserve reserveEdit = new MeetingReserve();
		reserveEdit.setId(param.getId());
		reserveEdit.setRetainedAudio(param.getRetainedAudio());
		meetingReserveRepository.updateById(reserveEdit);
		return true;
	}

	@Override
	public synchronized Boolean editServiceBeforeOrdinary(MeetingReserveAppSaveParam param) {
		MeetingReserve reserve = meetingReserveRepository.selectById(param.getId());
		reserveCheck(reserve);
		//状态检查
		AssertUtils.isTrue(MeetingReserveStatusEnum.START.getCode().equals(reserve.getStatus()), "会议状态已变更，无法更改会服需求");
		List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getReserveId, param.getId()).eq(MeetingAttendantTask::getServiceType, MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
		List<MeetingAttendantTaskDetail> taskDetails = CollectionUtil.isEmpty(tasks) ? new ArrayList<>() : meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId()).eq(MeetingAttendantTaskDetail::getServiceAttribute, MeetingAttendantTaskAttributeTypeEnum.ORDINARY.getValue()));
		checkTaskService(tasks);
		checkService(param);
		UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
		if(CollectionUtil.isNotEmpty(taskDetails)){
            meetingAttendantTaskDetailRepository.deleteBatchIds(taskDetails.stream().map(MeetingAttendantTaskDetail::getId).collect(Collectors.toList()));
		}
		if(CollectionUtil.isNotEmpty(param.getServiceParamList())){
			MeetingAttendantTask task = handleTask(reserve, tasks);
			List<MeetingAttendantTaskDetail> details = new ArrayList<>();
			param.getServiceParamList().forEach(service -> {
				handleTaskDetail(service, user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.ORDINARY.getName(), MeetingAttendantTaskAttributeEnum.ORDINARY.getValue(), MeetingAttendantTaskAttributeTypeEnum.ORDINARY.getValue(), details);
			});
			meetingAttendantTaskDetailService.addBatch(details);
		}else{
			if(CollectionUtil.isNotEmpty(tasks)){
				if(meetingAttendantTaskDetailService.count(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId())) < 1){
					this.removeById(tasks.get(0).getId());
				}
			}
		}
		return true;
	}
	@Override
	public synchronized Boolean editServiceBeforeSeat(MeetingReserveAppSaveParam param) {
		MeetingReserve reserve = meetingReserveRepository.selectById(param.getId());
		reserveCheck(reserve);
		//状态检查
		AssertUtils.isTrue(MeetingReserveStatusEnum.START.getCode().equals(reserve.getStatus()), "会议状态已变更，无法更改会议排座");
		List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getReserveId, param.getId()).eq(MeetingAttendantTask::getServiceType, MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
		List<MeetingAttendantTaskDetail> taskDetails = CollectionUtil.isEmpty(tasks) ? new ArrayList<>() : meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId()).eq(MeetingAttendantTaskDetail::getServiceAttribute, MeetingAttendantTaskAttributeTypeEnum.SEAT.getValue()));
		checkTaskService(tasks);
		UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
		meetingReserveSeatService.removeByReserveId(param.getId());
		if(CollectionUtil.isNotEmpty(param.getSeatParamList())){
			meetingReserveSeatService.addBatch(param.getId(), param.getSeatParamList());
			MeetingAttendantTask task = handleTask(reserve, tasks);
			if(CollectionUtil.isEmpty(taskDetails)){
				List<MeetingAttendantTaskDetail> details = new ArrayList<>();
				handleTaskDetail(null, user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.SEAT.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.SEAT.getValue(), details);
				meetingAttendantTaskDetailService.addBatch(taskDetails);
			}
		}else{
			if(CollectionUtil.isNotEmpty(taskDetails)){
				meetingAttendantTaskDetailService.removeById(taskDetails.get(0).getId());
				if(meetingAttendantTaskDetailService.count(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId()))< 1){
					this.removeById(tasks.get(0).getId());
				}
			}
		}
		return true;
	}

	@Override
	public synchronized Boolean editServiceBeforePrint(MeetingReserveAppSaveParam param) {
		MeetingReserve reserve = meetingReserveRepository.selectById(param.getId());
		reserveCheck(reserve);
		//状态检查
		AssertUtils.isTrue(MeetingReserveStatusEnum.START.getCode().equals(reserve.getStatus()), "会议状态已变更，无法更改会议文件");
		List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getReserveId, param.getId()).eq(MeetingAttendantTask::getServiceType, MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
		List<MeetingAttendantTaskDetail> taskDetails = CollectionUtil.isEmpty(tasks) ? new ArrayList<>() : meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId()).eq(MeetingAttendantTaskDetail::getServiceAttribute, MeetingAttendantTaskAttributeTypeEnum.PRINT.getValue()));
		checkTaskService(tasks);
        if(CollectionUtil.isEmpty(param.getFileParamList())){
            meetingReserveFileService.edit(param.getFileParam());
        }else{
            meetingReserveFileService.edits(param.getFileParamList());
        }
		List<MeetingReserveFileModel> fileModels = meetingReserveFileService.findByReserveId(param.getId());

		UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
		long filePintCount = CollectionUtil.isEmpty(fileModels) ? 0L :fileModels.stream().filter(item -> Status.enabled.getKey().equals(item.getPrinting())).count();
		if(filePintCount > 0L){
			MeetingAttendantTask task = handleTask(reserve, tasks);
			if(CollectionUtil.isEmpty(taskDetails)){
				List<MeetingAttendantTaskDetail> details = new ArrayList<>();
				handleTaskDetail(null, user, task.getId(), MeetingAttendantTaskAttributeTypeEnum.PRINT.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.PRINT.getValue(), details);
				meetingAttendantTaskDetailService.addBatch(taskDetails);
			}
		}else{
			if(CollectionUtil.isNotEmpty(taskDetails)){
				meetingAttendantTaskDetailService.removeById(taskDetails.get(0).getId());
				if(meetingAttendantTaskDetailService.count(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId()))<1){
					this.removeById(tasks.get(0).getId());
				}
			}
		}
		return true;
	}

	@Override
	public synchronized Boolean editServiceBeforeReserveName(MeetingReserveAppSaveParam param) {
		MeetingReserve reserve = meetingReserveRepository.selectById(param.getId());
		reserveCheck(reserve);
		//状态检查
		AssertUtils.isTrue(MeetingReserveStatusEnum.START.getCode().equals(reserve.getStatus()), "会议状态已变更，无法更改会议名称");
		MeetingReserve reserveEdit = new MeetingReserve();
		reserveEdit.setId(param.getId());
		if(ObjectUtil.isNotEmpty(param.getReserveName())){
			reserveEdit.setReserveName(param.getReserveName());
		}else{
			reserveEdit.setServeRemark(param.getServeRemark() == null ? "" : param.getServeRemark());
		}
		meetingReserveRepository.updateById(reserveEdit);
		if(ObjectUtil.isNotEmpty(param.getReserveName())){
			return this.update(Wrappers.<MeetingAttendantTask>lambdaUpdate().eq(MeetingAttendantTask::getReserveId, param.getId()).set(MeetingAttendantTask::getReserveName, param.getReserveName()));
		}
		return true;
	}
	private void reserveCheck(MeetingReserve reserve) {
		AssertUtils.isFalse(ObjectUtil.isEmpty(reserve) || !Objects.equals(WebFrameworkUtils.getHeaderTenantId(), reserve.getTenantId()), "当前园区无该会议预约信息，请知悉");
		AssertUtils.isFalse(Objects.equals(Status.enabled.getKey(), reserve.getCancelFlag()), "会议已取消，操作失败");
		AssertUtils.isTrue(reserve.getReserveUid().equals(WebFrameworkUtils.getHeaderUserId()), "非发起人，无权操作");
	}

	@Override
	public void handleServiceBeforePrint(Long reserveId, Long fileId){
		MeetingReserveFile file = meetingReserveFileService.getById(fileId);
		List<MeetingReserveFileModel> fileModels = meetingReserveFileService.findByReserveId(reserveId);
		long filePintCount = CollectionUtil.isEmpty(fileModels) ? 0L :fileModels.stream().filter(item ->!item.getId().equals(fileId) && Status.enabled.getKey().equals(item.getPrinting())).count();
		List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getReserveId, reserveId).eq(MeetingAttendantTask::getServiceType, MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
		List<MeetingAttendantTaskDetail> taskDetails = CollectionUtil.isEmpty(tasks) ? new ArrayList<>() : meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId()).eq(MeetingAttendantTaskDetail::getServiceAttribute, MeetingAttendantTaskAttributeTypeEnum.PRINT.getValue()));
		if(CollectionUtil.isNotEmpty(tasks) && !MeetingAttendantTaskStatusEnum.UNHANDLE.getValue().equals(tasks.get(0).getServiceStatus()) && Status.enabled.getKey().equals(file.getPrinting())){
			//会服确认后,需打印的文件无法删除,其他文件允许删除
			throw GenericException.fail("会服已确认，不支持修改");
		}
		if(CollectionUtil.isNotEmpty(taskDetails) && filePintCount <= 0L){
			meetingAttendantTaskDetailService.removeById(taskDetails.get(0).getId());
			if(meetingAttendantTaskDetailService.count(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, tasks.get(0).getId()))<1){
				this.removeById(tasks.get(0).getId());
			}
		}
	}

	@Override
	public void handleTaskCancelReserve(Long reserveId) {
		LambdaUpdateWrapper<MeetingAttendantTask> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(MeetingAttendantTask::getReserveId, reserveId)
				.eq(MeetingAttendantTask::getServiceType,MeetingAttendantTaskTypeEnum.BEFORE.getValue())
		        .ne(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
				.set(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
				.set(MeetingAttendantTask::getHandleTime, new Date())
				.set(MeetingAttendantTask::getHandleUname, "系统");
		this.update(wrapper);
	}

	@Override
	public List<MeetingAttendantTaskModel> findByReserveId(Long reserveId) {
		List<MeetingAttendantTask> list = list(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getReserveId, reserveId).orderByAsc(MeetingAttendantTask::getCreateTime));
		return BeanUtils.convertListTo(list, MeetingAttendantTaskModel::new);
	}

	@Override
	public void handleTaskEndReserve(MeetingReserve reserve) {
		LambdaUpdateWrapper<MeetingAttendantTask> wrapper = Wrappers.<MeetingAttendantTask>lambdaUpdate().eq(MeetingAttendantTask::getReserveId, reserve.getId())
				.ne(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
				.eq(MeetingAttendantTask::getServiceType,MeetingAttendantTaskTypeEnum.IN.getValue())
				.set(MeetingAttendantTask::getUpdateTime, new Date())
				.set(MeetingAttendantTask::getServiceValid, Status.disabled.getKey());
		this.update(wrapper);
		MeetingAttendantTask task = handleTask(reserve, MeetingAttendantTaskTypeEnum.AFTER.getValue());
		MeetingAttendantTaskDetail taskDetail = handleTaskDetail(null, null, task.getId(), task.getTenantId(), MeetingAttendantTaskAttributeTypeEnum.CLEAR.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.CLEAR.getValue());
		if(ObjectUtil.isNotEmpty(taskDetail)){
			meetingAttendantTaskDetailService.add(taskDetail);
		}
		//会服完成事件
		AttendantTaskEvent event = new AttendantTaskEvent();
		event.setRoomId(task.getRoomId());
		event.setServiceStatus(MeetingAttendantTaskStatusEnum.UNHANDLE.getValue());
		event.setServiceType(MeetingAttendantTaskTypeEnum.AFTER.getValue());
		asyncEventBus.post(event);
	}

	@Override
	public void handleTaskCallReserve(MeetingReserve reserve) {
		List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().eq(MeetingAttendantTask::getReserveId, reserve.getId())
				.eq(MeetingAttendantTask::getServiceType,MeetingAttendantTaskTypeEnum.IN.getValue())
				.eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.UNHANDLE.getValue()));
		MeetingAttendantTask task;
		if(CollectionUtil.isEmpty(tasks)){
			task = handleTask(reserve, MeetingAttendantTaskTypeEnum.IN.getValue());
			//临时呼叫通知(生成临时呼叫任务时通知)
			if (WebFrameworkUtils.getHeaderUserId() != null){
				List<SimpleMeetingAttendantModel> attendantList = meetingAttendantRoomService.listByRoomId(reserve.getRoomId());
				Set<String> userIdSet = attendantList.stream().map(SimpleMeetingAttendantModel::getUserId).collect(Collectors.toSet());
				UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
				Map<String, String> variables = new HashMap<>(4);
				variables.put("roomName",reserve.getRoomName());
				variables.put("reserveName",reserve.getReserveName());
				variables.put("staffid",user.getStaffid());
				variables.put("uname",user.getUserName());
				messageCommonService.sendMessage(MessageConstant.TEMP_CALL,reserve.getTenantId(),task.getId(),userIdSet,variables);
			}
		}else{
			task = tasks.get(0);
		}
		List<MeetingAttendantTaskDetail> taskDetails = new ArrayList<>();
		handleTaskDetail(null, getUser(WebFrameworkUtils.getHeaderUserId()), task.getId(), MeetingAttendantTaskAttributeTypeEnum.CALL.getName(), MeetingAttendantTaskAttributeEnum.DEFAULT.getValue(), MeetingAttendantTaskAttributeTypeEnum.CALL.getValue(), taskDetails);
		if(CollectionUtil.isNotEmpty(taskDetails)){
			meetingAttendantTaskDetailService.addBatch(taskDetails);
		}
		//会服完成事件
		AttendantTaskEvent event = new AttendantTaskEvent();
		event.setRoomId(task.getRoomId());
		event.setServiceStatus(MeetingAttendantTaskStatusEnum.UNHANDLE.getValue());
		event.setServiceType(MeetingAttendantTaskTypeEnum.IN.getValue());
		asyncEventBus.post(event);
	}

	@Override
	public List<MeetingAttendantTaskDetailModel> serviceDetailByTaskId(Long taskId) {
		List<MeetingAttendantTaskDetail> details = meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, taskId)
				.in(MeetingAttendantTaskDetail::getAttributeType, Arrays.asList(MeetingAttendantTaskAttributeTypeEnum.ORDINARY.getValue(), MeetingAttendantTaskAttributeTypeEnum.CALL.getValue(), MeetingAttendantTaskAttributeTypeEnum.CLEAR.getValue())));
		return BeanUtils.convertListTo(details, MeetingAttendantTaskDetailModel::new);
	}

	@Override
	public Boolean confirm(MeetingAttendantTaskParam param) {
		MeetingAttendantTask task = this.getById(param.getId());
		AssertUtils.isFalse(ObjectUtil.isEmpty(task) || !Objects.equals(WebFrameworkUtils.getHeaderTenantId(), task.getTenantId()), "当前园区无该会服信息，请知悉");
		//状态检查
        AssertUtils.isFalse(Status.disabled.getKey().equals(task.getServiceValid()),"会服已失效，无法确认");
		AssertUtils.isTrue(MeetingAttendantTaskStatusEnum.UNHANDLE.getValue().equals(task.getServiceStatus()), "会服状态已变更，无法确认");
		task.setServiceStatus(MeetingAttendantTaskStatusEnum.CONFIRM.getValue());
		task.setUpdateTime(new Date());
		return this.updateById(task);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean complete(MeetingAttendantTaskParam param) {
		MeetingAttendantTask task = this.getById(param.getId());
		AssertUtils.isFalse(ObjectUtil.isEmpty(task) || !Objects.equals(WebFrameworkUtils.getHeaderTenantId(), task.getTenantId()), "当前园区无该会服信息，请知悉");
        AssertUtils.isFalse(Status.disabled.getKey().equals(task.getServiceValid()),"会服已失效，无法完成");
		List<Long> serviceIds = new ArrayList<>();
		if(MeetingAttendantTaskTypeEnum.BEFORE.getValue().equals(task.getServiceType())){
			AssertUtils.isTrue(MeetingAttendantTaskStatusEnum.CONFIRM.getValue().equals(task.getServiceStatus()), "会服状态未确认，无法完成");
			List<MeetingAttendantTaskDetail> details = meetingAttendantTaskDetailService.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().eq(MeetingAttendantTaskDetail::getTaskId, task.getId()).eq(MeetingAttendantTaskDetail::getServiceAttribute, MeetingAttendantTaskAttributeTypeEnum.ORDINARY.getValue()));
			serviceIds = CollectionUtil.isEmpty(details) ? new ArrayList<>() : details.stream().map(MeetingAttendantTaskDetail::getServiceId).collect(Collectors.toList());
		}else{
			AssertUtils.isTrue(MeetingAttendantTaskStatusEnum.UNHANDLE.getValue().equals(task.getServiceStatus()), "会服状态已变更，无法完成");
		}
		task.setServiceStatus(MeetingAttendantTaskStatusEnum.COMPLETE.getValue());
		if(ObjectUtil.isNotEmpty(param.getServiceRemark())){
			task.setServiceRemark(param.getServiceRemark());
		}
		UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
		task.setHandleTime(new Date());
		task.setUpdateTime(new Date());
		task.setHandleUid(user.getId());
		task.setHandleUname(user.getUserName());
		task.setHandleStaffid(user.getStaffid());
		this.updateById(task);
		if(ObjectUtil.isNotEmpty(param.getRealParticipantNumber())){
			MeetingReserve reserve = new MeetingReserve();
			reserve.setId(task.getReserveId());
			reserve.setRealParticipantNumber(param.getRealParticipantNumber());
			meetingReserveRepository.updateById(reserve);
		}
		if(CollectionUtil.isNotEmpty(param.getFileParamList())){
			List<File> files = new ArrayList<>();
			param.getFileParamList().forEach(item->{
				File file = BeanUtils.convertTo(item, File::new);
				file.setRelatedId(param.getId());
				file.setType(FileTypeEnum.ATTENDANT.getValue());
				files.add(file);
			});
			fileService.addBatch(files);
		}
		//会服完成事件
		AttendantTaskEvent event = new AttendantTaskEvent();
		event.setRoomId(task.getRoomId());
		event.setServiceStatus(MeetingAttendantTaskStatusEnum.COMPLETE.getValue());
		event.setServiceType(task.getServiceType());
		event.setServiceIds(serviceIds);
		asyncEventBus.post(event);
		return true;
	}

	@Override
	public void handleTaskStartReserve(Long reserveId, Long roomId) {
		//失效该会议的会前服务
		LambdaUpdateWrapper<MeetingAttendantTask> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(MeetingAttendantTask::getReserveId, reserveId)
				.eq(MeetingAttendantTask::getRoomId, roomId)
				.eq(MeetingAttendantTask::getServiceType,MeetingAttendantTaskTypeEnum.BEFORE.getValue())
				.ne(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
				.ne(MeetingAttendantTask::getServiceValid, Status.disabled.getKey())
				.set(MeetingAttendantTask::getUpdateTime, new Date())
				.set(MeetingAttendantTask::getServiceValid, Status.disabled.getKey());
		this.update(wrapper);
		//失效其他会议的会后服务
		LambdaUpdateWrapper<MeetingAttendantTask> wrapper2 = new LambdaUpdateWrapper<>();
		wrapper2.eq(MeetingAttendantTask::getRoomId, roomId)
				.eq(MeetingAttendantTask::getServiceType,MeetingAttendantTaskTypeEnum.AFTER.getValue())
				.ne(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue())
				.ne(MeetingAttendantTask::getServiceValid, Status.disabled.getKey())
				.set(MeetingAttendantTask::getUpdateTime, new Date())
				.set(MeetingAttendantTask::getServiceValid, Status.disabled.getKey());
		this.update(wrapper2);
		//会服完成事件
		AttendantTaskEvent event = new AttendantTaskEvent();
		event.setRoomId(roomId);
		event.setServiceStatus(MeetingAttendantTaskStatusEnum.COMPLETE.getValue());
		event.setServiceType(MeetingAttendantTaskTypeEnum.BEFORE.getValue());
		event.setServiceValid(Status.disabled.getKey());
		asyncEventBus.post(event);
	}

	@Override
	public List<MeetingAttendantTask> findUnCompleteTask(List<Long> roomIdList) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		LambdaQueryWrapper<MeetingAttendantTask> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(MeetingAttendantTask::getServiceValid,Status.enabled.getKey()).eq(tenantId != null, MeetingAttendantTask::getTenantId, tenantId)
				.ne(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue()).in(MeetingAttendantTask::getRoomId, roomIdList);
		return list(wrapper);
	}

	@Override
	public List<MeetingAttendantTaskPageModel> callingList(MeetingAttendantTaskPageParam param) {
		param.setSize(Integer.MAX_VALUE);
		param.setServiceType(MeetingAttendantTaskTypeEnum.IN.getValue());
		if(!MeetingConstant.DESC.equals(param.getOrderByDirection())){
			param.setOrderByDirection(MeetingConstant.ASC);
		}
		return pageApp(param).getRecords();
	}

	@Override
	public IPage<MeetingAttendantTaskPageModel> pageHistory(MeetingAttendantTaskPageParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		param.setUserId(WebFrameworkUtils.getHeaderUserId());

		IPage<MeetingAttendantTaskPageModel> page = getBaseMapper().pageHistory(new Page<>(param.getCurrent(), param.getSize()), param);
		List<MeetingAttendantTaskPageModel> list = page.getRecords();
		if (CollectionUtil.isEmpty(list)) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(), list);
	}

	@Override
	public MeetingAttendantTaskCountModel taskCompleteCount() {
		MeetingAttendantTaskCountModel model = new MeetingAttendantTaskCountModel();
		List<MeetingAttendantRoomModel> roomModelList = meetingAttendantRoomService.listByUserIdIn(Collections.singletonList(WebFrameworkUtils.getHeaderUserId()));
		if(CollectionUtil.isNotEmpty(roomModelList)){
			model.setRoomNum((long) roomModelList.size());
		}
		List<MeetingAttendantTask> tasks = this.list(Wrappers.<MeetingAttendantTask>lambdaQuery().select(MeetingAttendantTask::getId, MeetingAttendantTask::getReserveId).eq(MeetingAttendantTask::getHandleUid, WebFrameworkUtils.getHeaderUserId()).eq(MeetingAttendantTask::getServiceStatus, MeetingAttendantTaskStatusEnum.COMPLETE.getValue()));
		if(CollectionUtil.isNotEmpty(tasks)){
			List<Long> reserveIds = tasks.stream().map(MeetingAttendantTask::getReserveId).distinct().collect(Collectors.toList());
			model.setTaskNum((long) tasks.size());
			model.setReserveNum((long) reserveIds.size());
			List<MeetingReserve> meetingReserves = meetingReserveRepository.selectList(Wrappers.<MeetingReserve>lambdaQuery().select(MeetingReserve::getId, MeetingReserve::getRealParticipantNumber).in(MeetingReserve::getId, reserveIds));
			model.setPersonNum(meetingReserves.stream().mapToLong(MeetingReserve::getRealParticipantNumber).sum());
			List<MeetingAttendantEvaluate> evaluates = meetingAttendantEvaluateService.list(Wrappers.<MeetingAttendantEvaluate>lambdaQuery().select(MeetingAttendantEvaluate::getId, MeetingAttendantEvaluate::getScore).in(MeetingAttendantEvaluate::getReserveId, reserveIds));
			if(CollectionUtil.isNotEmpty(evaluates)){
				OptionalDouble average = evaluates.stream().mapToInt(MeetingAttendantEvaluate::getScore).average();
				average.ifPresent(avg -> model.setScore(Math.round(avg * 100.0) / 100.0));
			}
		}
		return model;
	}

	private void checkTaskService(List<MeetingAttendantTask> tasks){
		if(CollectionUtil.isNotEmpty(tasks) && !MeetingAttendantTaskStatusEnum.UNHANDLE.getValue().equals(tasks.get(0).getServiceStatus())){
			throw GenericException.fail("会服已确认，不支持修改");
		}
		if(CollectionUtil.isNotEmpty(tasks) && Status.disabled.getKey().equals(tasks.get(0).getServiceValid())){
			throw GenericException.fail("会服已失效，不支持修改");
		}
	}

	private MeetingAttendantTask handleTask(MeetingReserve reserve, Integer taskType){
		MeetingAttendantTask task = new MeetingAttendantTask();
		task.setReserveId(reserve.getId());
		task.setReserveName(reserve.getReserveName());
		task.setRoomId(reserve.getRoomId());
		task.setRoomName(reserve.getRoomName());
		task.setServiceType(taskType);
		task.setTenantId(reserve.getTenantId());
		this.save(task);
		return task;
	}

	private MeetingAttendantTask handleTask(MeetingReserve reserve, List<MeetingAttendantTask> tasks){
		MeetingAttendantTask task;
		if(CollectionUtil.isEmpty(tasks)){
			task = new MeetingAttendantTask();
			task.setReserveId(reserve.getId());
			task.setReserveName(reserve.getReserveName());
			task.setRoomId(reserve.getRoomId());
			task.setRoomName(reserve.getRoomName());
			task.setServiceType(MeetingAttendantTaskTypeEnum.BEFORE.getValue());
			task.setTenantId(reserve.getTenantId());
			this.save(task);
		}else{
			task = tasks.get(0);
		}
		return task;
	}

	/***
	 * @Description 获取用户信息
	 * @author huangyongtao
	 * @date 2024/8/2 10:34
	 * @param userId
	 */
	private UserInfoModel getUser(String userId) {
		return Objects.requireNonNull(userApiService.detail(userId));
	}

	private Integer handleDuration(Date beginTime, Date endTime){
		// 计算两个日期之间的分钟差
		long minutesBetween = DateUtil.between(beginTime, endTime, DateUnit.MINUTE);
		// 定义一个数组，存储每个区间的分钟数
		int[] intervals = {1, 5, 10, 15, 30, 60, 120};
		// 遍历数组，找到第一个大于 minutesBetween 的区间
		for (int i = 0; i < intervals.length; i++) {
			if (minutesBetween <= intervals[i]) {
				return intervals[i];
			}
		}
		// 如果没有找到合适的区间，返回默认值
		return 120;
	}
}
