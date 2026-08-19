
package com.cgnpc.bbxpark.workorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.DateUtil;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.energy.domain.EnergyAbnormalRemind;
import com.cgnpc.bbxpark.energy.domain.MeterAutoRecord;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecord;
import com.cgnpc.bbxpark.energy.service.IEnergyAbnormalRemindService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordService;
import com.cgnpc.bbxpark.energy.service.impl.MeterAutoRecordServiceImpl;
import com.cgnpc.bbxpark.problemReport.domain.ProblemDevice;
import com.cgnpc.bbxpark.problemReport.domain.ProblemHandleRecord;
import com.cgnpc.bbxpark.problemReport.domain.ProblemReport;
import com.cgnpc.bbxpark.problemReport.service.IProblemDeviceService;
import com.cgnpc.bbxpark.problemReport.service.IProblemHandleRecordService;
import com.cgnpc.bbxpark.problemReport.service.IProblemReportService;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FileParam;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.settings.service.impl.DictServiceImpl;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderRoman;
import com.cgnpc.bbxpark.workorder.domain.WorkPlanDetail;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderDeviceModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanDetailModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderDeviceParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPlanHandleParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkPlanDetailParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkPlanDetailRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderDeviceService;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderRomanService;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import com.cgnpc.bbxpark.workorder.service.IWorkPlanDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/***
 * @Description 工单计划详细信息服务实现
 * @author huangyongtao
 * @date 2025/3/25 16:41
 */
@Service("workPlanDetailService")
@Slf4j
public class WorkPlanDetailServiceImpl extends ServiceImpl<WorkPlanDetailRepository, WorkPlanDetail> implements IWorkPlanDetailService {

	@Autowired
	private IWorkOrderDeviceService workOrderDeviceService;

	@Autowired
	private IWorkOrderRomanService workOrderRomanService;

	@Autowired
	private IWorkOrderService workOrderService;

	@Autowired
	private IFileService fileService;

	@Autowired
	private IIocDeviceService iocDeviceService;

	@Autowired
	private IocDeviceRepository iocDeviceRepository;

	@Autowired
	private IProblemReportService problemReportService;

	@Autowired
	private IProblemDeviceService problemDeviceService;

	@Autowired
	private IProblemHandleRecordService problemHandleRecordService;

//	@Autowired
//	private UserInfoFeignClient userInfoFeignClient;

    @Autowired
    private IUserApiService userApiService;

	@Autowired
	private IMessageCommonService messageCommonService;

	@Autowired
	private IAttentionManageService attentionManageService;

	@Autowired
	private IMeterPersonRecordService meterPersonRecordService;
    @Autowired
    private MeterAutoRecordServiceImpl meterAutoRecordService;
	@Autowired
	private IEnergyAbnormalRemindService energyAbnormalRemindService;
//	@Autowired
//	private DictCategoryFeignClient dictCategoryFeignClient;
//	@Autowired
//	private DictItemFeignClient dictItemFeignClient;
    @Autowired
    private DictServiceImpl dictService;

	/**
	 * 根据工单计划详细信息标识获得工单计划详细信息详情信息.
	 * @Param [id] 工单计划详细信息标识
	 * @Return 工单计划详细信息详情信息
	 */
	@Override
	public WorkPlanDetailModel detail(WorkPlanDetailParam param) {
		AssertUtils.notNull(param.getWorkId(), "工单id不能为空");
		List<WorkPlanDetail> details = this.list(buildQuery(param));
		AssertUtils.notEmpty(details, "工单不存在");
		WorkPlanDetailModel model = BeanUtils.convertTo(details.get(0), WorkPlanDetailModel::new);
		//工单设备
		List<WorkOrderDevice> workOrderDevices = workOrderDeviceService.list(new LambdaQueryWrapper<WorkOrderDevice>().eq(WorkOrderDevice::getWorkOrderId, param.getWorkId()));
		List<WorkOrderDeviceModel> workOrderDeviceModels = CollectionUtil.isEmpty(workOrderDevices) ? CollectionUtil.newArrayList() :BeanUtils.convertListTo(workOrderDevices, WorkOrderDeviceModel::new);
		model.setWorkOrderDeviceList(workOrderDeviceModels);
		model.setWorkOrderDeviceNum((long) workOrderDeviceModels.size());

		//计划生成的第一个工单
		WorkPlanDetail detail = this.getOne(Wrappers.<WorkPlanDetail>lambdaQuery().select(WorkPlanDetail::getId, WorkPlanDetail::getCreateTime)
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
	public WorkOrderModel findWorkOrderDeviceList(WorkOrderPlanHandleParam param) {
		WorkOrder workOrder = workOrderService.getById(param.getId());
		AssertUtils.notNull(workOrder, "工单不存在");
		WorkOrderModel workOrderModel = BeanUtils.convertTo(workOrder, WorkOrderModel::new);
		List<WorkOrderDevice> workOrderDevices = workOrderDeviceService.list(new LambdaQueryWrapper<WorkOrderDevice>().eq(WorkOrderDevice::getWorkOrderId, param.getId()));
		if(CollectionUtil.isEmpty(workOrderDevices)){
			return workOrderModel;
		}
		List<WorkOrderDeviceModel> workOrderDeviceModels = BeanUtils.convertListTo(workOrderDevices, WorkOrderDeviceModel::new);
		List<Long> ids = workOrderDeviceModels.stream().map(WorkOrderDeviceModel::getId).collect(Collectors.toList());
		List<FileModel> fileModels = fileService.findByTypeAndRelatedIds(FileTypeEnum.WORKPLANDEVICE.getValue(),ids);
		List<Long> deviceIds = workOrderDeviceModels.stream().map(WorkOrderDeviceModel::getDeviceId).collect(Collectors.toList());
		List<IocDevice> iocDevices = iocDeviceService.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getReadingValue, IocDevice::getReadingTime).in(IocDevice::getId, deviceIds));
		Map<Long, IocDevice> iocDeviceMap = iocDevices.stream().collect(Collectors.toMap(IocDevice::getId, f -> f));
		Map<Long, List<FileModel> > fileModelMap = CollectionUtil.isEmpty(fileModels) ? new HashMap<>() : fileModels.stream().collect(Collectors.groupingBy(FileModel::getRelatedId));
		workOrderDeviceModels.forEach(workOrderDeviceModel -> {
			workOrderDeviceModel.setFileModelList(fileModelMap.containsKey(workOrderDeviceModel.getId()) ? fileModelMap.get(workOrderDeviceModel.getId()) : null);
			workOrderDeviceModel.setOldReadingTime(iocDeviceMap.containsKey(workOrderDeviceModel.getDeviceId()) ? iocDeviceMap.get(workOrderDeviceModel.getDeviceId()).getReadingTime() : null);
			BigDecimal oldReadingValue = iocDeviceMap.containsKey(workOrderDeviceModel.getDeviceId()) ? iocDeviceMap.get(workOrderDeviceModel.getDeviceId()).getReadingValue() : null;
			BigDecimal readingValue = workOrderDeviceModel.getReadingValue();
			if(DeviceReadingTypeEnum.ELECTRICITY.getCode().equals(workOrderDeviceModel.getReadingType())){
				oldReadingValue = oldReadingValue == null ? null : oldReadingValue.setScale(2);
				readingValue = readingValue == null ? null : readingValue.setScale(2);
			}else{
				oldReadingValue = oldReadingValue == null ? null : oldReadingValue.setScale(3);
				readingValue = readingValue == null ? null : readingValue.setScale(3);
			}
			workOrderDeviceModel.setOldReadingValue(oldReadingValue);
			workOrderDeviceModel.setReadingValue(readingValue);
		});
		workOrderModel.setWorkOrderDeviceModels(workOrderDeviceModels);
        //工单计划详情
        WorkPlanDetailParam planDetailParam = new WorkPlanDetailParam();
        planDetailParam.setWorkId(param.getId());
        List<WorkPlanDetail> details = this.list(buildQuery(planDetailParam));
        workOrderModel.setWorkPlanDetailModel(CollectionUtil.isEmpty(details) ? new WorkPlanDetailModel() : BeanUtils.convertTo(details.get(0), WorkPlanDetailModel::new));
		return workOrderModel;
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean saveWorkPlan(WorkOrderPlanHandleParam param) {
		List<WorkOrderDeviceParam> deviceList = param.getWorkOrderDeviceList();
		//校验工单
		checkWorkParam(param.getId(),  userApiService.getCurrentStaffNo(), deviceList);
		if(ObjectUtil.isNotEmpty(param.getProcessedDesc()) || ObjectUtil.isNotEmpty(param.getOutReason())){
			workOrderService.update(new LambdaUpdateWrapper<WorkOrder>().set(ObjectUtil.isNotEmpty(param.getOutReason()), WorkOrder::getOutReason, param.getOutReason())
					.set(ObjectUtil.isNotEmpty(param.getProcessedDesc()), WorkOrder::getProcessedDesc, param.getProcessedDesc())
					.eq(WorkOrder::getId, param.getId()));
		}
		List<WorkOrderDevice> workOrderDevices = new ArrayList<>();
		List<File> files = new ArrayList<>();
		List<Long> relatedIds = new ArrayList<>();
		deviceList.forEach(deviceParam->{
			if(ObjectUtil.isNotEmpty(deviceParam.getReadingErrorStatus()) || ObjectUtil.isNotEmpty(deviceParam.getReadingErrorRemark()) || ObjectUtil.isNotEmpty(deviceParam.getReadingValue()) || ObjectUtil.isNotEmpty(deviceParam.getFileParamList())){
				workOrderDevices.add(handleWorkOrderDevice(deviceParam));
			}
			if(CollectionUtil.isNotEmpty(deviceParam.getFileParamList())){
				relatedIds.add(deviceParam.getId());
				deviceParam.getFileParamList().forEach(item->{
					files.add(handleFile(item, deviceParam.getId(), FileTypeEnum.WORKPLANDEVICE.getValue()));
				});
			}
		});
		//更新工单的抄表设备
		batchPersist(workOrderDevices, workOrderDeviceService::updateBatchById);
		//删除工单抄表设备图片
		batchRemoveRelatedFiles(FileTypeEnum.WORKPLANDEVICE, relatedIds);
		//新增抄表设备图片
		batchPersist(files, fileService::addBatch);
		return true;
	}
	@Transactional(rollbackFor = Exception.class)
	@Override
	public synchronized Boolean submitWorkPlan(WorkOrderPlanHandleParam handleParam) {
        String userId = userApiService.getCurrentStaffNo();
		List<WorkOrderDeviceParam> params = handleParam.getWorkOrderDeviceList();
		//校验工单
		WorkOrder workOrder = checkWorkParam(handleParam.getId(), userId, params);
		//校验工单超时原因
		checkWordOderOut(workOrder, handleParam);
		//校验工单设备
		checkDeviceParam(params);
		//初始化上下文
		UserInfoModel userInfo = getUser(userId);
		Date now = new Date();
		ProcessingContext context = new ProcessingContext(userInfo, now);
		//批量处理设备数据
		processDevices(params, context, workOrder);
		//数据持久化
		persistData(context);
		//更新工单状态
		handleWorkOder(workOrder, now, handleParam);
		//保存工单流程
		handleWorkOderRoman(userInfo, handleParam, workOrder);
		//发送特别关注人消息
		sendAttentionMessage(context.problemReports, userInfo, context.problemDeviceMap);
		return true;
	}

	private void persistData(ProcessingContext context) {
		//保存报事保修设备
		batchPersist(context.problemDevices, problemDeviceService::saveBatch);
		//保存报事报修图片
		batchPersist(context.problemReportFiles, fileService::addBatch);
		//保存报事保修流程
		batchPersist(context.problemRecords, problemHandleRecordService::saveBatch);
		//更新设备的抄表记录
		batchPersist(context.updatedDevices, iocDeviceRepository::batchUpdateDevices);
		//更新工单的抄表设备
		batchPersist(context.modifiedDevices, workOrderDeviceService::updateBatchById);
		//删除工单抄表设备图片
		batchRemoveRelatedFiles(FileTypeEnum.WORKPLANDEVICE, context.deviceFileRelatedIds);
		//保存工单抄表设备图片
		batchPersist(context.workOrderDeviceFiles, fileService::addBatch);
		//保存抄表人工抄表记录
		batchPersist(context.meterPersonRecords, meterPersonRecordService::saveBatch);
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

	private void processDevices(List<WorkOrderDeviceParam> params, ProcessingContext context, WorkOrder workOrder) {
		List<Long> deviceIds = params.stream().map(WorkOrderDeviceParam::getDeviceId).collect(Collectors.toList());
		List<IocDevice> iocDevices = iocDeviceService.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getIotDevicePlatform).in(IocDevice::getId, deviceIds));
		context.iocDeviceIotPlatformMap.putAll(iocDevices.stream().collect(Collectors.toMap(IocDevice::getId, IocDevice::getIotDevicePlatform)));
		params.forEach(param -> {
			// 更新工单设备基础信息
			context.modifiedDevices.add(handleWorkOrderDevice(param));
			context.deviceFileRelatedIds.add(param.getId());

			if (isProblemDevice(param)) {
                workOrder.setErrorStatus(Status.enabled.getKey());
				handleProblemReportFlow(param, context);
			} else if (hasReadingValue(param)) {
				handleIocDevice(param, context, workOrder);
			}
		});
	}

	private boolean isProblemDevice(WorkOrderDeviceParam param) {
		return Integer.valueOf(Status.enabled.getKey()).equals(param.getReadingErrorStatus());
	}

	private boolean hasReadingValue(WorkOrderDeviceParam param) {
		return ObjectUtil.isNotEmpty(param.getReadingValue());
	}

	private void handleProblemReportFlow(WorkOrderDeviceParam param, ProcessingContext context) {
		ProblemReport report = createProblemReport(param, context);
		context.problemReports.add(report);
		context.problemDevices.add(handleProblemDevice(param, report.getId()));
		context.problemDeviceMap.put(report.getId(), param.getDeviceName());
		param.getFileParamList().forEach(fileParam -> {
			context.problemReportFiles.add(handleFile(fileParam, report.getId(), FileTypeEnum.PROBLEMREPORT.getValue()));
			context.workOrderDeviceFiles.add(handleFile(fileParam, param.getId(), FileTypeEnum.WORKPLANDEVICE.getValue()));
		});
		context.problemRecords.add(handleProblemHandleRecord(report.getId(), context));
	}

	private static class ProcessingContext {
		final UserInfoModel user;
		final Date timestamp;
		final List<ProblemDevice> problemDevices = new ArrayList<>();
		final List<File> problemReportFiles = new ArrayList<>();
		final List<File> workOrderDeviceFiles = new ArrayList<>();
		final List<ProblemHandleRecord> problemRecords = new ArrayList<>();

		final List<ProblemReport> problemReports = new ArrayList<>();
		final List<IocDevice> updatedDevices = new ArrayList<>();
		final List<WorkOrderDevice> modifiedDevices = new ArrayList<>();
		final List<Long> deviceFileRelatedIds = new ArrayList<>();

		final Map<Long, String> problemDeviceMap = new HashMap<>();

		final List<MeterPersonRecord> meterPersonRecords = new ArrayList<>();

		final Map<Long, Integer> iocDeviceIotPlatformMap = new HashMap<>();

		ProcessingContext(UserInfoModel user, Date timestamp) {
			this.user = user;
			this.timestamp = timestamp;
		}
	}

	private WorkOrderDevice handleWorkOrderDevice(WorkOrderDeviceParam param) {
		WorkOrderDevice workOrderDevice = new WorkOrderDevice();
		workOrderDevice.setId(param.getId());
		workOrderDevice.setReadingValue(param.getReadingValue());
		workOrderDevice.setReadingErrorStatus(param.getReadingErrorStatus());
		workOrderDevice.setReadingErrorRemark(param.getReadingErrorRemark());
		return workOrderDevice;
	}
	private ProblemDevice handleProblemDevice(WorkOrderDeviceParam param, Long problemId) {
		ProblemDevice problemDevice = new ProblemDevice();
		problemDevice.setDeviceId(param.getDeviceId());
		problemDevice.setDeviceName(param.getDeviceName());
		problemDevice.setSpaceId(param.getSpaceId());
		problemDevice.setSpaceName(param.getSpaceFullPath());
		problemDevice.setProblemId(problemId);
		return problemDevice;
	}

	private ProblemReport createProblemReport(WorkOrderDeviceParam param, ProcessingContext context) {
		ProblemReport problemReport = new ProblemReport();
		problemReport.setProblemDesc(param.getReadingErrorRemark());
		problemReport.setSpaceName(param.getSpaceFullPath());
		problemReport.setSpaceId(param.getSpaceId());
		problemReport.setProblemType(1);
		problemReport.setStatus(ProblemStatusEnum.NEW.getCode());
		problemReport.setCreateBy(context.user.getUserName());
		problemReport.setCreatorId(context.user.getStaffid());
		problemReport.setCreatorId(context.user.getId());
		problemReport.setCreateTime(context.timestamp);
		problemReportService.save(problemReport);
		return problemReport;
	}

	private File handleFile(FileParam param, Long relatedId, Integer type) {
		File file = BeanUtils.convertTo(param, File::new);
		file.setRelatedId(relatedId);
		file.setType(type);
		return file;
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

	private void handleIocDevice(WorkOrderDeviceParam param, ProcessingContext context, WorkOrder workOrder) {
		IocDevice iocDevice = new IocDevice();
		iocDevice.setId(param.getDeviceId());
		iocDevice.setReadingValue(param.getReadingValue());
		iocDevice.setReadingTime(context.timestamp);
        List<String> urls = new ArrayList<>();
        param.getFileParamList().forEach(fileParam -> {
            context.workOrderDeviceFiles.add(handleFile(fileParam, param.getId(), FileTypeEnum.WORKPLANDEVICE.getValue()));
            urls.add(fileParam.getUrl());
        });
        MeterPersonRecord record = handleMeterPersonRecord(param, context);
        record.setWorkOrderId(workOrder.getId());
        record.setWorkOrderName(workOrder.getName());
        if(CollectionUtil.isNotEmpty(urls)){
            iocDevice.setReadingImg(String.join(",",urls));
            record.setReadingImg(iocDevice.getReadingImg());
        }
        context.meterPersonRecords.add(record);
        context.updatedDevices.add(iocDevice);
		//判断是否生成能耗异常提醒
		energyAbnormalRemind(param);
	}

	private void energyAbnormalRemind(WorkOrderDeviceParam param) {
		try {
			//根据设备id查询当前小时内该设备自动上报的读数数据，对比差额
//			DictCategoryListParam dictCategoryListParam = new DictCategoryListParam();
//			dictCategoryListParam.setCode("EnergyAbnormalRemind");
//			List<DictCategoryModel> dictCategoryModels = dictCategoryFeignClient.list(dictCategoryListParam).getBody().getResult();
//			if (CollectionUtils.isEmpty(dictCategoryModels)) {
//				log.info("未找到字典分类能耗异常提醒");
//				return;
//			}
//			DictCategoryModel dictCategoryModel = dictCategoryModels.get(0);
//			DictItemListParam dictParam = new DictItemListParam();
//			dictParam.setDictTypeId(dictCategoryModel.getId());
//			List<DictItemModel> result = dictItemFeignClient.list(dictParam).getBody().getResult();
//			if (CollectionUtils.isEmpty(result)) {
//				log.info("未找到字典分类能耗异常提醒");
//				return;
//			}
            List<DictItemModel> result = dictService.findItemsByDictType("EnergyAbnormalRemind");
            if (CollectionUtils.isEmpty(result)) {
				log.info("未找到字典分类能耗异常提醒");
				return;
			}
			//获取字典配置能耗偏差定额
			BigDecimal quota = null;
			for (DictItemModel dictItemModel : result) {
				if (dictItemModel.getLabel().equals("抄表异常定额")) {
					quota = new BigDecimal(dictItemModel.getValue().replace("%", "")).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					break;
				}
			}
			if (quota == null) {
				log.info("未找到能耗偏差定额,抄表异常判定结束");
				return;
			}
			//获取当前时间小时的开始和结束时间
			Date startTime = DateUtil.getFirstTimeOfCurrent();
			Date endTime = DateUtil.getLastTimeOfCurrent();
			List<MeterAutoRecord> meterAutoRecordList = meterAutoRecordService.list(new LambdaQueryWrapper<MeterAutoRecord>().eq(MeterAutoRecord::getDeviceId, param.getDeviceId()).between(MeterAutoRecord::getCreateTime, startTime, endTime).orderByDesc(MeterAutoRecord::getCreateTime).last("limit 1"));
			if (ObjectUtil.isNotEmpty(meterAutoRecordList)) {
				MeterAutoRecord meterAutoRecord = meterAutoRecordList.get(0);
				//自动读数减去人工上报读书除以自动读数计算占比
				BigDecimal proportion = (meterAutoRecord.getReadingValue().subtract(param.getReadingValue())).abs().divide(meterAutoRecord.getReadingValue(), 2, RoundingMode.HALF_UP);
				//如果占比大于0.1则生成抄表异常数据
				if (proportion.compareTo(quota) > 0) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//生成抄表异常数据
					EnergyAbnormalRemind abnormalRemind = new EnergyAbnormalRemind();
					abnormalRemind.setRemindName("抄表异常提醒");
					abnormalRemind.setRemindContent("设备上报的数据和人工抄表数据差额超出设定值；" + formatter.format(new Date()) + "【" + param.getDeviceName() + "】人工抄表数据为" + param.getReadingValue() + "，自动上报数据为" + meterAutoRecord.getReadingValue() + " ，设定差额为" + quota.multiply(new BigDecimal("100")) + "%，实际差额为" + proportion.multiply(new BigDecimal("100")) + "%");
					abnormalRemind.setStatus(0);
					abnormalRemind.setTenantId(meterAutoRecord.getTenantId());
					abnormalRemind.setCreateTime(new Date());
					abnormalRemind.setDeviceId(String.valueOf(meterAutoRecord.getDeviceId()));
//					abnormalRemind.setDeviceName(meterAutoRecord.getDeviceName());
					abnormalRemind.setSpaceId(meterAutoRecord.getSpaceId());
					abnormalRemind.setSpaceName(meterAutoRecord.getSpaceName());
                    abnormalRemind.setRemindType(meterAutoRecord.getReadingType());
					energyAbnormalRemindService.save(abnormalRemind);
				}
			}
		} catch (Exception e) {
			log.error("抄表异常提醒判断报错", e);
		}
	}

	private MeterPersonRecord handleMeterPersonRecord(WorkOrderDeviceParam param, ProcessingContext context) {
		MeterPersonRecord meterPersonRecord = new MeterPersonRecord();
		meterPersonRecord.setDeviceId(param.getDeviceId());
		meterPersonRecord.setDeviceName(param.getDeviceName());
		meterPersonRecord.setSpaceName(param.getSpaceFullPath());
		meterPersonRecord.setSpaceId(param.getSpaceId());
		meterPersonRecord.setReadingType(param.getReadingType());
		meterPersonRecord.setReadingCode(param.getReadingCode());
		meterPersonRecord.setReadingRate(param.getReadingRate());
		meterPersonRecord.setReadingValue(param.getReadingValue());
		meterPersonRecord.setDeviceType(DevicePlatformEnum.NO.getCode().equals(context.iocDeviceIotPlatformMap.get(param.getDeviceId())) ? Status.disabled.getKey() : Status.enabled.getKey());
		meterPersonRecord.setCreatorId(context.user.getId());
		meterPersonRecord.setCreateTime(context.timestamp);
		meterPersonRecord.setUpdateTime(context.timestamp);
		meterPersonRecord.setUpdatorId(context.user.getId());
		return meterPersonRecord;
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
		workOrderRoman.setRedundancyFour(handleParam.getOutReason()+"");
		workOrderRoman.setRemark(handleParam.getProcessedDesc());
		workOrderRomanService.save(workOrderRoman);

	}

	private WorkOrder checkWorkParam(Long workOrderId, String userId, List<WorkOrderDeviceParam> params) {
		AssertUtils.notNull(workOrderId, "工单id不能为空");
		WorkOrder workOrder = workOrderService.getById(workOrderId);
		AssertUtils.notNull(workOrder, "工单不存在");
        log.info("工单id:{}, 工单处理人：{}， 当前登录账号：{}",workOrder.getId(), workOrder.getProcessedPersonId(), userId);
		AssertUtils.isTrue(userId.equals(workOrder.getProcessedPersonId()), "非当前处理人，无权限处理");
		AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.PROCESSING.getCode()), "该工单状态已变更,请确认");
		AssertUtils.notEmpty(params, "抄表设备不能为空");
		return workOrder;
	}

	private void checkDeviceParam(List<WorkOrderDeviceParam> params) {
		for (WorkOrderDeviceParam param : params) {
			AssertUtils.notEmpty(param.getFileParamList(), param.getDeviceName() + "请上传图片");
			AssertUtils.isFalse(ObjectUtil.isEmpty(param.getReadingValue()) && ObjectUtil.isEmpty(param.getReadingErrorStatus()), param.getDeviceName() + "请抄表");
		}
	}

	private void checkWordOderOut(WorkOrder workOrder, WorkOrderPlanHandleParam handleParam) {
		AssertUtils.isFalse(Integer.valueOf(Status.enabled.getKey()).equals(workOrder.getOutStatus()) && ObjectUtil.isEmpty(handleParam.getOutReason()), "该工单已超时，请确认超时原因");
	}

	private LambdaQueryWrapper buildQuery(WorkPlanDetailParam param) {
		LambdaQueryWrapper<WorkPlanDetail> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(param.getWorkId()), WorkPlanDetail::getWorkId, param.getWorkId());
		query.eq(ObjectUtil.isNotEmpty(param.getPlanId()), WorkPlanDetail::getPlanId, param.getPlanId());
		return query;
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

	/***
	 * @Description 关注人消息
	 * @author huangyongtao
	 * @date 2025/3/31 16:02
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
}
