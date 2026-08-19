
package com.cgnpc.bbxpark.workorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.domain.MaterialInbound;
import com.cgnpc.bbxpark.property.domain.MaterialRecord;
import com.cgnpc.bbxpark.property.service.IMaterialRecordService;
import com.cgnpc.bbxpark.property.service.IMaterialService;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FileParam;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.DateUtil;
import com.cgnpc.bbxpark.problemReport.domain.ProblemDevice;
import com.cgnpc.bbxpark.problemReport.domain.ProblemHandleRecord;
import com.cgnpc.bbxpark.problemReport.domain.ProblemReport;
import com.cgnpc.bbxpark.problemReport.service.IProblemDeviceService;
import com.cgnpc.bbxpark.problemReport.service.IProblemHandleRecordService;
import com.cgnpc.bbxpark.problemReport.service.IProblemReportService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.domain.*;
import com.cgnpc.bbxpark.workorder.dto.model.*;
import com.cgnpc.bbxpark.workorder.dto.param.*;
import com.cgnpc.bbxpark.workorder.mapper.WorkTaskRepository;
import com.cgnpc.bbxpark.workorder.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/***
 * @Description 工单任务服务实现
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
@Slf4j
@Service("workTaskService")
public class WorkTaskServiceImpl extends ServiceImpl<WorkTaskRepository, WorkTask> implements IWorkTaskService {

	@Autowired
	private IWorkPlanDetailService workPlanDetailService;

	@Autowired
	private IWorkOrderService workOrderService;

	@Autowired
	private IWorkMaterialService workMaterialService;

	@Autowired
	private IWorkTaskItemService workTaskItemService;

	@Autowired
	private IFileService fileService;

//	@Autowired
//	private UserInfoFeignClient userInfoFeignClient;

    @Autowired
    private IUserApiService userApiService;

	@Autowired
	private IMessageCommonService messageCommonService;

	@Autowired
	private IAttentionManageService attentionManageService;

	@Autowired
	private IProblemReportService problemReportService;

	@Autowired
	private IProblemDeviceService problemDeviceService;

	@Autowired
	private IProblemHandleRecordService problemHandleRecordService;
	@Autowired
	private IParkSpaceService parkSpaceService;

	@Autowired
	private IWorkOrderRomanService workOrderRomanService;

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private IMaterialRecordService materialRecordService;
	/**
	 * 获取工单任务列表.
	 * @Param workId 工单标识
	 * @Return 工单任务信息列表
	 */
	@Override
	@SneakyThrows
	public List<WorkTaskModel> list(Long workId) {
		List<WorkTask> workTasks = this.list(buildQuery(workId));
		return BeanUtils.convertListTo(workTasks, WorkTaskModel::new);
	}

	/**
	 * 批量新增工单任务.
	 * @Param params 工单任务信息列表
	 * @Return 批量新增工单任务是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<WorkTaskParam> params) {
		if (CollectionUtil.isEmpty(params)) {
			return true;
		}
		List<WorkTask> workTasks = BeanUtils.convertListTo(params, WorkTask::new);
		return this.saveBatch(workTasks);
	}

	/**
	 * 根据工单计划详细信息标识获得工单计划详细信息详情信息.
	 * @Param [id] 工单计划详细信息标识
	 * @Return 工单计划详细信息详情信息
	 */
	@Override
	public WorkPlanDetailModel detail(WorkPlanDetailParam param) {
		AssertUtils.notNull(param.getWorkId(), "工单id不能为空");
		List<WorkPlanDetail> details = workPlanDetailService.list(Wrappers.<WorkPlanDetail>lambdaQuery().eq(WorkPlanDetail::getWorkId, param.getWorkId()));
		AssertUtils.notEmpty(details, "工单不存在");
		WorkPlanDetailModel model = BeanUtils.convertTo(details.get(0), WorkPlanDetailModel::new);

		List<WorkTaskModel> taskModels =  this.list(param.getWorkId());
        if(model.getPlanType().equals(WorkOrderSourceEnum.PATROLPLAN.getCode())){
            taskModels = CollectionUtil.isEmpty(taskModels) ? CollectionUtil.newArrayList() :  taskModels.stream().filter(p->ObjectUtil.isNotEmpty(p.getName())).collect(Collectors.toList());
        }
		Map<Integer, List<WorkTaskModel>> taskMap = CollectionUtil.isEmpty(taskModels) ? new LinkedHashMap<>() : taskModels.stream().collect(Collectors.groupingBy(WorkTaskModel::getTaskGroup, LinkedHashMap::new, Collectors.toList()));
		List<WorkTaskItemModel> taskItemModels = workTaskItemService.list(param.getWorkId());
		Map<Integer, List<WorkTaskItemModel>> taskItemMap = CollectionUtil.isEmpty(taskItemModels) ? new LinkedHashMap<>() : taskItemModels.stream().collect(Collectors.groupingBy(WorkTaskItemModel::getTaskGroup, LinkedHashMap::new, Collectors.toList()));
		List<WorkTaskGroupModel> workTaskGroupModels = new ArrayList<>();
		taskMap.forEach((key, value) -> {
			WorkTaskGroupModel workTaskGroupModel = new WorkTaskGroupModel();
			workTaskGroupModel.setTaskGroup(key);
			workTaskGroupModel.setWorkTaskModels(value);
			workTaskGroupModel.setName(value.get(0).getName());
			workTaskGroupModel.setType(WorkTaskTypeEnum.INVENTORY_DEVICE.getCode().equals(value.get(0).getBusinessType()) ? 2 : 1);
			workTaskGroupModel.setWorkTaskItemModels(taskItemMap.get(key));
			workTaskGroupModel.setTaskItemNum((CollectionUtil.isEmpty(taskItemMap.get(key)) ? 0L :  (long)taskItemMap.get(key).size()));
			workTaskGroupModel.setTaskNum(((long)value.size()));
			workTaskGroupModels.add(workTaskGroupModel);
		});
		//工单任务组
		model.setWorkTaskGroupModels(workTaskGroupModels);
		//工单材料
		model.setWorkMaterialModels( workMaterialService.list(param.getWorkId()));
		//计划生成的第一个工单
		WorkPlanDetail detail = workPlanDetailService.getOne(Wrappers.<WorkPlanDetail>lambdaQuery().select(WorkPlanDetail::getId, WorkPlanDetail::getCreateTime)
				.eq(WorkPlanDetail::getPlanId, model.getPlanId())
				.eq(WorkPlanDetail::getDeleted, Status.enabled.getKey())
				.orderByAsc(WorkPlanDetail::getCreateTime)
				.last("LIMIT 1"));
		model.setWorkPlanFirstTime(ObjectUtil.isEmpty(detail) ? null : detail.getCreateTime());

		//计划工单完成的次数
		model.setWorkPlanCompleteNum((long) workOrderService.count(new LambdaQueryWrapper<WorkOrder>().eq(WorkOrder::getBusinessId, model.getPlanId())
				.eq(WorkOrder::getDeleted, Status.enabled.getKey())
				.eq(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())));
		return model;
	}

	@Override
	public WorkOrderModel findWorkPlan(WorkOrderPlanHandleParam param) {
		WorkOrder workOrder = workOrderService.getById(param.getId());
		AssertUtils.notNull(workOrder, "工单不存在");
		WorkOrderModel workOrderModel = BeanUtils.convertTo(workOrder, WorkOrderModel::new);
		List<WorkTaskModel> taskModels =  this.list(param.getId());
        if(workOrderModel.getSource().equals(WorkOrderSourceEnum.PATROLPLAN.getCode())){
            taskModels = CollectionUtil.isEmpty(taskModels) ? CollectionUtil.newArrayList() :  taskModels.stream().filter(p->ObjectUtil.isNotEmpty(p.getName())).collect(Collectors.toList());
        }
		Map<Long, List<FileModel>> fileTaskMap = new HashMap<>();
		if(CollectionUtil.isNotEmpty(taskModels)){
			List<FileModel> fileTaskModels = fileService.findByTypeAndRelatedIds(FileTypeEnum.WORKPLANTASK.getValue(), taskModels.stream().map(WorkTaskModel::getId).collect(Collectors.toList()));
			fileTaskMap  = CollectionUtil.isEmpty(fileTaskModels) ? new HashMap<>() : fileTaskModels.stream().collect(Collectors.groupingBy(FileModel::getRelatedId));
		}
		Map<Integer, List<WorkTaskModel>> taskMap = CollectionUtil.isEmpty(taskModels) ? new LinkedHashMap<>() : taskModels.stream().collect(Collectors.groupingBy(WorkTaskModel::getTaskGroup, LinkedHashMap::new, Collectors.toList()));
		List<WorkTaskItemModel> taskItemModels = workTaskItemService.list(param.getId());
		Map<Long, List<FileModel>> fileTaskItemMap = new HashMap<>();
		if(CollectionUtil.isNotEmpty(taskItemModels)){
			List<FileModel> fileTaskItemModels = fileService.findByTypeAndRelatedIds(FileTypeEnum.WORKPLANTASKITEM.getValue(), taskItemModels.stream().map(WorkTaskItemModel::getId).collect(Collectors.toList()));
			fileTaskItemMap  = CollectionUtil.isEmpty(fileTaskItemModels) ? new HashMap<>() : fileTaskItemModels.stream().collect(Collectors.groupingBy(FileModel::getRelatedId));
		}
		Map<Integer, List<WorkTaskItemModel>> taskItemMap = CollectionUtil.isEmpty(taskItemModels) ? new LinkedHashMap<>() : taskItemModels.stream().collect(Collectors.groupingBy(WorkTaskItemModel::getTaskGroup, LinkedHashMap::new, Collectors.toList()));
		List<WorkTaskGroupModel> workTaskGroupModels = new ArrayList<>();
		Map<Long, List<FileModel>> finalFileTaskMap = fileTaskMap;
		Map<Long, List<FileModel>> finalFileTaskItemMap = fileTaskItemMap;
		taskMap.forEach((key, value) -> {
			WorkTaskGroupModel workTaskGroupModel = new WorkTaskGroupModel();
			workTaskGroupModel.setTaskGroup(key);
			value.forEach(workTaskModel -> workTaskModel.setFileModelList((finalFileTaskMap.containsKey(workTaskModel.getId()) ? finalFileTaskMap.get(workTaskModel.getId()) : null)));
			workTaskGroupModel.setWorkTaskModels(value);
			workTaskGroupModel.setName(value.get(0).getName());
			workTaskGroupModel.setType(WorkTaskTypeEnum.INVENTORY_DEVICE.getCode().equals(value.get(0).getBusinessType()) ? 2 : 1);
			workTaskGroupModel.setWorkTaskItemModels(taskItemMap.get(key));
			if(CollectionUtil.isNotEmpty(workTaskGroupModel.getWorkTaskItemModels())){
				workTaskGroupModel.getWorkTaskItemModels().forEach(workTaskItemModel -> workTaskItemModel.setFileModelList((finalFileTaskItemMap.containsKey(workTaskItemModel.getId()) ? finalFileTaskItemMap.get(workTaskItemModel.getId()) : null)));
			}
			workTaskGroupModel.setTaskItemNum((CollectionUtil.isEmpty(taskItemMap.get(key)) ? 0L :  (long)taskItemMap.get(key).size()));
			workTaskGroupModel.setTaskNum(((long)value.size()));
			workTaskGroupModels.add(workTaskGroupModel);
		});
		//工单任务组
		workOrderModel.setWorkTaskGroupModels(workTaskGroupModels);
		//工单材料
		workOrderModel.setWorkMaterialModels( workMaterialService.list(param.getId()));
		return workOrderModel;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean saveWorkPlan(WorkOrderPlanHandleParam param) {
		List<WorkTaskParam> taskParams = param.getWorkTaskParams();
		List<WorkTaskItemParam> taskItemParams = param.getWorkTaskItemParams();
		//校验工单
		checkWorkParam(param.getId(), userApiService.getCurrentStaffNo());
		if(ObjectUtil.isNotEmpty(param.getProcessedDesc()) || ObjectUtil.isNotEmpty(param.getOutReason())){
			workOrderService.update(new LambdaUpdateWrapper<WorkOrder>().set(ObjectUtil.isNotEmpty(param.getOutReason()), WorkOrder::getOutReason, param.getOutReason())
					.set(ObjectUtil.isNotEmpty(param.getProcessedDesc()), WorkOrder::getProcessedDesc, param.getProcessedDesc())
					.eq(WorkOrder::getId, param.getId()));
		}
		List<WorkTask> taskList = new ArrayList<>();
		List<File> filesTask = new ArrayList<>();
		List<Long> relatedTaskIds = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(taskParams)) {
            taskParams.forEach(taskParam -> {
                if (ObjectUtil.isNotEmpty(taskParam.getErrorStatus()) || ObjectUtil.isNotEmpty(taskParam.getErrorRemark()) || ObjectUtil.isNotEmpty(taskParam.getFileParamList())) {
                    taskList.add(handleWorkTask(taskParam));
                }
                if (CollectionUtil.isNotEmpty(taskParam.getFileParamList())) {
                    relatedTaskIds.add(taskParam.getId());
                    taskParam.getFileParamList().forEach(item -> {
                        filesTask.add(handleFile(item, taskParam.getId(), FileTypeEnum.WORKPLANTASK.getValue()));
                    });
                }
            });
            //更新工单异常任务
            batchPersist(taskList, this::updateBatchById);
            //删除工单任务异常图片
            batchRemoveRelatedFiles(FileTypeEnum.WORKPLANTASK, relatedTaskIds);
            //新增工单任务异常图片
            batchPersist(filesTask, fileService::addBatch);
        }
		List<WorkTaskItem> taskItemList = new ArrayList<>();
		List<File> filesTaskItem = new ArrayList<>();
		List<Long> relatedTaskItemIds = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(taskItemParams)){
            taskItemParams.forEach(taskItemParam->{
                if(ObjectUtil.isNotEmpty(taskItemParam.getErrorStatus()) || ObjectUtil.isNotEmpty(taskItemParam.getErrorRemark()) || ObjectUtil.isNotEmpty(taskItemParam.getFileParamList())){
                    taskItemList.add(handleWorkTaskItem(taskItemParam));
                }
                if(CollectionUtil.isNotEmpty(taskItemParam.getFileParamList())){
                    relatedTaskItemIds.add(taskItemParam.getId());
                    taskItemParam.getFileParamList().forEach(item->{
                        filesTaskItem.add(handleFile(item, taskItemParam.getId(), FileTypeEnum.WORKPLANTASKITEM.getValue()));
                    });
                }
            });
            //更新工单异常任务项
            batchPersist(taskItemList, workTaskItemService::updateBatchById);
            //删除工单任务项异常图片
            batchRemoveRelatedFiles(FileTypeEnum.WORKPLANTASKITEM, relatedTaskItemIds);
            //新增工单任务项异常图片
            batchPersist(filesTaskItem, fileService::addBatch);
        }
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public synchronized Boolean submitWorkPlan(WorkOrderPlanHandleParam handleParam) {
        String userId = userApiService.getCurrentStaffNo();
		List<WorkTaskParam> taskParams = handleParam.getWorkTaskParams();
		List<WorkTaskItemParam> taskItemParams = handleParam.getWorkTaskItemParams();
		//校验工单
		WorkOrder workOrder = checkWorkParam(handleParam.getId(), userId);
		//校验工单超时原因
		checkWordOderOut(workOrder, handleParam);
		//校验工单任务
		checkTaskParam(taskParams, taskItemParams);
		//初始化上下文
		UserInfoModel userInfo = getUser(userId);
		Date now = new Date();
		ProcessingContext context = new ProcessingContext(userInfo, now);
		//批量处理任务数据
		processTasks(taskParams, taskItemParams, context, workOrder);
		//数据持久化
		persistData(context);
		//更新工单状态
		handleWorkOder(workOrder, now, handleParam);
        //更新工单材料
        handleWorkMaterial(handleParam, userInfo);
		//保存工单流程
		handleWorkOderRoman(userInfo, handleParam, workOrder);
		//发送特别关注人消息
		sendAttentionMessage(context.problemReports, userInfo, context.problemDeviceMap);
		return true;
	}

	private void handleWorkOder(WorkOrder workOrder, Date now, WorkOrderPlanHandleParam handleParam){
		Integer status;
		if(ObjectUtil.isEmpty(workOrder.getAuditUid())){
			status = WorkOrderStatusEnum.COMPLETED.getCode();
		}else{
			status = WorkOrderStatusEnum.AUDIT.getCode();
		}
		double expendTime = DateUtil.calculateHourDifference(workOrder.getAcceptTime(),now);
		workOrderService.update(new LambdaUpdateWrapper<WorkOrder>().set(ObjectUtil.isNotEmpty(handleParam.getOutReason()), WorkOrder::getOutReason, handleParam.getOutReason())
				.set(ObjectUtil.isNotEmpty(handleParam.getProcessedDesc()), WorkOrder::getProcessedDesc, handleParam.getProcessedDesc())
				.set(WorkOrder::getStatus, status)
				.set(WorkOrderStatusEnum.COMPLETED.getCode().equals(status), WorkOrder::getEndTime,now)
				.set(WorkOrder::getUpdateTime, now)
				.set(WorkOrderStatusEnum.COMPLETED.getCode().equals(status), WorkOrder::getExpendTime, expendTime)
                .set(ObjectUtil.isNotEmpty(workOrder.getErrorStatus()), WorkOrder::getErrorStatus, workOrder.getErrorStatus())
				.eq(WorkOrder::getId, handleParam.getId()));
		if(WorkOrderStatusEnum.COMPLETED.getCode().equals(status)){
			//工单完成通知
			Map<String, String> variables = new HashMap<>();
			variables.put("name", workOrder.getName());
			variables.put("code", workOrder.getCode());
			messageCommonService.sendMessage(MessageConstant.ORDER_COMPLETED_NOTICE, workOrder.getTenantId(), workOrder.getId(), workOrder.getCreatorId(), variables);
		}
	}
    private void handleWorkMaterial(WorkOrderPlanHandleParam handleParam, UserInfoModel userInfo){
        List<WorkMaterialParam> workMaterialParams = handleParam.getWorkMaterialParams();
        if( CollectionUtil.isEmpty(workMaterialParams)){
            return;
        }
        workMaterialService.updateBatchById(BeanUtils.convertListTo(workMaterialParams, WorkMaterial::new));
        List<Long> materialIds = workMaterialParams.stream().map(WorkMaterialParam::getMaterialId).collect(Collectors.toList());
        List<Material> materials = (List<Material>)materialService.listByIds(materialIds);
        Map<Long, Material> materialMap = CollectionUtil.isEmpty(materials) ? new HashMap<>() : materials.stream().collect(Collectors.toMap(Material::getId, v -> v));
        List<Material> materialUpdates = new ArrayList<>();
        List<MaterialRecord> materialRecords = new ArrayList<>();
        workMaterialParams.forEach(param -> {
            Material material = materialMap.get(param.getMaterialId());
            //出库材料库存更新
            Material materialUpdate = new Material();
            materialUpdate.setId(material.getId());
            materialUpdate.setStockQuantity(material.getStockQuantity() - param.getMaterialUseNum());
            materialUpdate.setStockWarning(material.getStockWarning());
            materialUpdate.setOutbound(material.getOutbound() + 1);
            materialUpdate.setOutboundQuantity(material.getOutboundQuantity() + param.getMaterialUseNum());
            materialService.handelStockStatus(materialUpdate);
            materialUpdates.add(materialUpdate);
            //出库记录
            MaterialRecord materialRecord = new MaterialRecord();
            BeanUtils.copyProperties(param, materialRecord);
            materialRecord.setQuantity(param.getMaterialUseNum());
            materialRecord.setCreateBy(userInfo.getUserName());
            materialRecord.setRecordType(MaterialRecordTypeEnum.OUT.getCode());
            materialRecord.setDataSource(MaterialRecordSourceEnum.WORK.getCode());
            materialRecord.setCurrentStock(materialUpdate.getStockQuantity());
            materialRecords.add(materialRecord);
        });
		if (CollectionUtil.isNotEmpty(materialUpdates)) {
            materialService.updateBatchById(materialUpdates);
        }
		if(CollectionUtil.isNotEmpty(materialRecords)){
            materialRecordService.saveBatch(materialRecords);
        }
    }

	private void handleWorkOderRoman(UserInfoModel userInfo, WorkOrderPlanHandleParam handleParam, WorkOrder workOrder){
		WorkOrderRoman workOrderRoman = new WorkOrderRoman();
		workOrderRoman.setId(null);
		workOrderRoman.setWorkOrderId(handleParam.getId());
		if(ObjectUtil.isEmpty(workOrder.getAuditUid())){
			workOrderRoman.setRomanStatus(WorkOrderStatusEnum.COMPLETED.getCode());
            workOrderRoman.setOperatorId(userInfo.getId());
            workOrderRoman.setOperatorName(userInfo.getUserName());
            workOrderRoman.setOperatorStaffid(userInfo.getStaffid());
            workOrderRoman.setOperator("处理人");
            workOrderRoman.setOperatorValue(userInfo.getUserName());
		}else{
			workOrderRoman.setRomanStatus(WorkOrderStatusEnum.AUDIT.getCode());
            workOrderRoman.setOperatorId(workOrder.getAuditUid());
            workOrderRoman.setOperatorName(workOrder.getAuditUname());
            workOrderRoman.setOperatorStaffid(workOrder.getAuditStaffid());
            workOrderRoman.setOperator("审核人");
            workOrderRoman.setOperatorValue(workOrder.getAuditUname());
		}
		workOrderRoman.setRedundancyFour(ObjectUtil.isEmpty(handleParam.getOutReason())? "" : handleParam.getOutReason()+"");
		workOrderRoman.setRemark(handleParam.getProcessedDesc());
		workOrderRomanService.save(workOrderRoman);

	}
	private void persistData(ProcessingContext context) {
		//保存报事保修设备
		batchPersist(context.problemDevices, problemDeviceService::saveBatch);
		//保存报事报修图片
		batchPersist(context.problemReportFiles, fileService::addBatch);
		//保存报事保修流程
		batchPersist(context.problemRecords, problemHandleRecordService::saveBatch);
		//更新工单的任务
		batchPersist(context.modifiedWorkTasks, this::updateBatchById);
		//更新工单的任务项
		batchPersist(context.modifiedWorkTaskItems, workTaskItemService::updateBatchById);
		//删除工单任务异常图片
		batchRemoveRelatedFiles(FileTypeEnum.WORKPLANTASK, context.taskFileRelatedIds);
		//删除工单任务项异常图片
		batchRemoveRelatedFiles(FileTypeEnum.WORKPLANTASKITEM, context.taskItemFileRelatedIds);
		//保存工单任务异常图片
		batchPersist(context.workTaskFiles, fileService::addBatch);
		//保存工单任务项异常图片
		batchPersist(context.workTaskItemFiles, fileService::addBatch);
	}

	private void processTasks(List<WorkTaskParam> params,List<WorkTaskItemParam> taskItemParams,  ProcessingContext context, WorkOrder workOrder) {
        if(CollectionUtil.isNotEmpty(params)){
            List<Long> errorSpaceIds = params.stream().map(WorkTaskParam::getErrorSpaceId).collect(Collectors.toList());
            Map<Long, ParkSpaceFullModel> fullSpaceMap = CollectionUtil.isEmpty(errorSpaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(errorSpaceIds, null);
            params.forEach(param -> {
                // 更新工单设备基础信息
                context.modifiedWorkTasks.add(handleWorkTask(param));
                context.taskFileRelatedIds.add(param.getId());
                if (Status.enabled.getKey().equals(param.getErrorStatus())) {
                    workOrder.setErrorStatus(Status.enabled.getKey());
                }
                if (isProblem(param.getProblemStatus())) {
                    handleProblemReportFlowTask(param, fullSpaceMap, context);
                }
                param.getFileParamList().forEach(fileParam -> {
                    context.workTaskFiles.add(handleFile(fileParam, param.getId(), FileTypeEnum.WORKPLANTASK.getValue()));
                });
            });
        }
        if(CollectionUtil.isNotEmpty(taskItemParams)){
            taskItemParams.forEach(param -> {
                // 更新工单设备基础信息
                context.modifiedWorkTaskItems.add(handleWorkTaskItem(param));
                context.taskItemFileRelatedIds.add(param.getId());
                if (Status.enabled.getKey().equals(param.getErrorStatus())) {
                    workOrder.setErrorStatus(Status.enabled.getKey());
                }
                if (isProblem(param.getProblemStatus())) {
                    handleProblemReportFlowTaskItem(param, context);
                }
                param.getFileParamList().forEach(fileParam -> {
                    context.workTaskItemFiles.add(handleFile(fileParam, param.getId(), FileTypeEnum.WORKPLANTASKITEM.getValue()));
                });
            });
        }
	}
	private boolean isProblem(Integer status) {
		return Integer.valueOf(Status.enabled.getKey()).equals(status);
	}

	private void handleProblemReportFlowTask(WorkTaskParam param, Map<Long, ParkSpaceFullModel> fullSpaceMap, ProcessingContext context) {
		Long spaceId = ObjectUtil.isEmpty(param.getErrorSpaceId()) ? param.getSpaceId() : param.getErrorSpaceId();
		String spaceName = ObjectUtil.isEmpty(param.getErrorSpaceId()) ? param.getSpaceFullPath() : fullSpaceMap.get(spaceId).getSpaceName();
		ProblemReport report = createProblemReport(param.getErrorRemark(), spaceId, spaceName, context);
		context.problemReports.add(report);
		if(WorkTaskTypeEnum.MAINTAIN_DEVICE.getCode().equals(param.getBusinessType()) || WorkTaskTypeEnum.INVENTORY_DEVICE.getCode().equals(param.getBusinessType())){
			context.problemDevices.add(handleProblemDevice(param.getBusinessId(), param.getName(), param.getSpaceId(), param.getSpaceFullPath(), report.getId()));
			context.problemDeviceMap.put(report.getId(), param.getName());
		}
		param.getFileParamList().forEach(fileParam -> {
			context.problemReportFiles.add(handleFile(fileParam, report.getId(), FileTypeEnum.PROBLEMREPORT.getValue()));
		});
		context.problemRecords.add(handleProblemHandleRecord(report.getId(), context));
	}

	private void handleProblemReportFlowTaskItem(WorkTaskItemParam param, ProcessingContext context) {
		ProblemReport report = createProblemReport(param.getErrorRemark(), param.getSpaceId(), param.getSpaceFullPath(), context);
		context.problemReports.add(report);
		param.getFileParamList().forEach(fileParam -> {
			context.problemReportFiles.add(handleFile(fileParam, report.getId(), FileTypeEnum.PROBLEMREPORT.getValue()));
		});
		context.problemRecords.add(handleProblemHandleRecord(report.getId(), context));
	}

	private ProblemHandleRecord handleProblemHandleRecord(Long problemId, ProcessingContext context) {
		ProblemHandleRecord problemHandleRecord = new ProblemHandleRecord();
		problemHandleRecord.setProblemId(problemId);
		problemHandleRecord.setOperateTime(context.timestamp);
		problemHandleRecord.setOperator(context.user.getUserName());
		problemHandleRecord.setOperatorStaffid(context.user.getStaffid());
		problemHandleRecord.setLink(ProblemStatusEnum.NEW.getCode());
		problemHandleRecord.setCreateBy(context.user.getUserName());
		problemHandleRecord.setCreateTime(context.timestamp);
		problemHandleRecord.setCreatorId(context.user.getId());
		return problemHandleRecord;
	}

	private ProblemReport createProblemReport(String remark, Long spaceId, String spaceFullPath, ProcessingContext context) {
		ProblemReport problemReport = new ProblemReport();
		problemReport.setProblemDesc(remark);
		problemReport.setSpaceName(spaceFullPath);
		problemReport.setSpaceId(spaceId);
		problemReport.setProblemType(1);
		problemReport.setStatus(ProblemStatusEnum.NEW.getCode());
		problemReport.setCreateBy(context.user.getUserName());
		problemReport.setCreatorId(context.user.getStaffid());
		problemReport.setCreatorId(context.user.getId());
		problemReport.setCreateTime(context.timestamp);
		problemReportService.save(problemReport);
		return problemReport;
	}

	private ProblemDevice handleProblemDevice(Long id, String name, Long spaceId, String spaceFullPath, Long problemId) {
		ProblemDevice problemDevice = new ProblemDevice();
		problemDevice.setDeviceId(id);
		problemDevice.setDeviceName(name);
		problemDevice.setSpaceId(spaceId);
		problemDevice.setSpaceName(spaceFullPath);
		problemDevice.setProblemId(problemId);
		return problemDevice;
	}


	/***
	 * @Description 关注人消息
	 * @author huangyongtao
	 * @date 2025/11/11 16:02
	 * @param problemReports
	 */
	private void sendAttentionMessage(List<ProblemReport> problemReports, UserInfoModel userInfo, Map<Long, String> problemDeviceMap) {
		if(CollectionUtil.isEmpty(problemReports) || !attentionManageService.checkAttention(userInfo.getId())){
			return;
		}
		problemReports.forEach(problemReport -> {
			Map<String, String> variables = new HashMap<>(4);
			variables.put("attentionName", userInfo.getUserName());
			variables.put("content", problemReport.getProblemDesc());
			variables.put("type", "问题报修");
			variables.put("device", ObjectUtil.isNotEmpty(problemDeviceMap.get(problemReport.getId())) ? problemDeviceMap.get(problemReport.getId()) : "无");
			variables.put("spaceName", problemReport.getSpaceName());
			messageCommonService.sendMessage(MessageConstant.ATTENTION_REPORT_NOTICE, problemReport.getTenantId(), problemReport.getId(), new HashSet<>(), variables);
		});
	}


	private static class ProcessingContext {
		final UserInfoModel user;
		final Date timestamp;
		final List<ProblemDevice> problemDevices = new ArrayList<>();
		final List<File> problemReportFiles = new ArrayList<>();
		final List<File> workTaskFiles = new ArrayList<>();
		final List<File> workTaskItemFiles = new ArrayList<>();
		final List<ProblemHandleRecord> problemRecords = new ArrayList<>();

		final List<ProblemReport> problemReports = new ArrayList<>();
		final List<WorkTask> modifiedWorkTasks = new ArrayList<>();
		final List<WorkTaskItem> modifiedWorkTaskItems = new ArrayList<>();
		final List<Long> taskFileRelatedIds = new ArrayList<>();
		final List<Long> taskItemFileRelatedIds = new ArrayList<>();

		final Map<Long, String> problemDeviceMap = new HashMap<>();

		ProcessingContext(UserInfoModel user, Date timestamp) {
			this.user = user;
			this.timestamp = timestamp;
		}
	}
	private WorkTask handleWorkTask(WorkTaskParam param) {
		WorkTask workTask = new WorkTask();
		workTask.setId(param.getId());
		workTask.setErrorStatus(param.getErrorStatus());
		workTask.setErrorRemark(param.getErrorRemark());
        workTask.setProblemStatus(param.getProblemStatus());
		return workTask;
	}

	private WorkTaskItem handleWorkTaskItem(WorkTaskItemParam param) {
		WorkTaskItem workTaskItem = new WorkTaskItem();
		workTaskItem.setId(param.getId());
		workTaskItem.setErrorStatus(param.getErrorStatus());
        workTaskItem.setErrorRemark(param.getErrorRemark());
        workTaskItem.setProblemStatus(param.getProblemStatus());
		return workTaskItem;
	}

	private File handleFile(FileParam param, Long relatedId, Integer type) {
		File file = BeanUtils.convertTo(param, File::new);
		file.setRelatedId(relatedId);
		file.setType(type);
		return file;
	}


	private <T> void batchPersist(List<T> data, Consumer<List<T>> persister) {
		if (CollectionUtil.isNotEmpty(data)) {
			persister.accept(data);
		}
	}
	private void batchRemoveRelatedFiles(FileTypeEnum fileType, List<Long> relatedIds) {
		if (CollectionUtil.isNotEmpty(relatedIds)) {
			fileService.removeByRelatedIds(fileType.getValue(), relatedIds);
		}
	}

	/***
	 * @Description 获取用户信息
	 * @author huangyongtao
	 * @date 2024/8/2 10:34
	 * @param userId
	 */
	private UserInfoModel getUser(String userId) {
		return Objects.requireNonNull(userApiService.getByStaffNo(userId));
	}
	private WorkOrder checkWorkParam(Long workOrderId, String userId) {
		AssertUtils.notNull(workOrderId, "工单id不能为空");
		WorkOrder workOrder = workOrderService.getById(workOrderId);
		AssertUtils.notNull(workOrder, "工单不存在");
        log.info("工单id:{}, 工单处理人：{}， 当前登录账号：{}",workOrder.getId(), workOrder.getProcessedPersonId(), userId);
		AssertUtils.isTrue(userId.equals(workOrder.getProcessedPersonId()), "非当前处理人，无权限处理");
		AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.PROCESSING.getCode()), "该工单状态已变更,请确认");
		return workOrder;
	}

	private void checkWordOderOut(WorkOrder workOrder, WorkOrderPlanHandleParam handleParam) {
		AssertUtils.isFalse(Integer.valueOf(Status.enabled.getKey()).equals(workOrder.getOutStatus()) && ObjectUtil.isEmpty(handleParam.getOutReason()), "该工单已超时，请确认超时原因");
	}

	private void checkTaskParam(List<WorkTaskParam> params, List<WorkTaskItemParam> taskItemParams) {
        if(CollectionUtil.isNotEmpty(params)){
            for (WorkTaskParam param : params) {
                if(Integer.valueOf(Status.enabled.getKey()).equals(param.getErrorStatus()) && ObjectUtil.isEmpty(param.getName())){
                    AssertUtils.notEmpty(param.getFileParamList(), param.getName() + "请上传异常图片");
                    AssertUtils.isFalse(ObjectUtil.isEmpty(param.getErrorRemark()), param.getName() + "请填写异常说明");
                    AssertUtils.isFalse(ObjectUtil.isEmpty(param.getSpaceId()) && ObjectUtil.isEmpty(param.getErrorSpaceId()), param.getName() + "请选择异常位置");
                }
            }
        }
		if(CollectionUtil.isNotEmpty(taskItemParams)){
			for (WorkTaskItemParam param : taskItemParams) {
				if(Integer.valueOf(Status.enabled.getKey()).equals(param.getErrorStatus()) && ObjectUtil.isEmpty(param.getName())){
					AssertUtils.notEmpty(param.getFileParamList(), param.getName() + "请上传异常图片");
					AssertUtils.isFalse(ObjectUtil.isEmpty(param.getErrorRemark()), param.getName() + "请填写异常说明");
				}
			}
		}
	}


	private LambdaQueryWrapper<WorkTask> buildQuery(Long workId) {
		LambdaQueryWrapper<WorkTask> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(workId), WorkTask::getWorkId, workId);
		query.orderByAsc(WorkTask::getTaskGroup);
		return query;
	}
}
