package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.constant.WorkOrderCodePrefixConstant;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.property.domain.InspectionPlan;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.dto.model.InspectionMaterialModel;
import com.cgnpc.bbxpark.property.dto.model.InspectionPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.InspectionPlanModel;
import com.cgnpc.bbxpark.property.dto.model.InspectionPointModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.mapper.InspectionPlanRepository;
import com.cgnpc.bbxpark.property.service.*;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.workorder.domain.WorkMaterial;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkTask;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkMaterialParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskParam;
import com.cgnpc.bbxpark.workorder.service.IWorkMaterialService;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import com.cgnpc.bbxpark.workorder.service.IWorkTaskService;
import com.cgnpc.framework.permission.holder.DataSqlHandlerHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡检计划管理服务实现
 */
@Slf4j
@Service
public class InspectionPlanServiceImpl extends ServiceImpl<InspectionPlanRepository, InspectionPlan> implements IInspectionPlanService {
    @Resource
	private IInspectionPointService inspectionPointService;
	@Resource
	private IInspectionMaterialService inspectionMaterialService;
	@Resource
	private IPropertyScheduleService propertyScheduleService;
	@Resource
	private IUserApiService userApiService;

	@Resource
	private IWorkOrderService workOrderService;

    @Resource
    private IMaterialService materialService;

    @Resource
    private IWorkMaterialService workMaterialService;

    @Resource
    private IWorkTaskService workTaskService;

	/**
	 * 根据巡检计划管理标识获得巡检计划管理详情信息.
	 * @Param [id] 巡检计划管理标识
	 * @Return 巡检计划管理详情信息
	 */
	@Override
	public InspectionPlanModel detail(Long id) {
		InspectionPlan inspectionPlan = this.getById(id);
		AssertUtils.notNull(inspectionPlan, SystemResultCode.RESULT_DATA_NONE.message());
		InspectionPlanModel model = BeanUtils.convertTo(inspectionPlan, InspectionPlanModel::new);
		//巡检材料及巡检点信息
		model.setMaterialModels(materialHandle(id));
		model.setPointModels(pointHandle(inspectionPlan.getPointId()));
		return model;
	}

	/**
	 * 获取巡检计划管理列表(分页).
	 * @Param param 巡检计划管理查询条件
	 * @Return 巡检计划管理信息列表（分页）
	 */
	@Override
	public IPage<InspectionPlanListModel> page(InspectionPlanPageParam param) {
		IPage<InspectionPlan> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, InspectionPlanListParam::new)));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result, convert(result.getRecords()));
	}

	/**
	 * 获取巡检计划管理列表.
	 * @Param param 巡检计划管理查询条件
	 * @Return 巡检计划管理信息列表
	 */
	@Override
	@SneakyThrows
	public List<InspectionPlanListModel> list(InspectionPlanListParam param) {
		List<InspectionPlan> plans = this.list(buildQuery(param));
		return BeanUtils.convertListTo(plans, InspectionPlanListModel::new);
	}

	/**
	 * 新增巡检计划管理.
	 * @Param param 巡检计划管理信息
	 * @Return 新增巡检计划管理是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean add(InspectionPlanParam param) {
		InspectionPlan inspectionPlan = BeanUtils.convertTo(param, InspectionPlan::new);
		//创建人
		UserInfoModel userInfo = userApiService.getCurrentUserInfo();
        inspectionPlan.setCreateBy(userInfo.getUserName());
		inspectionPlan.setId(null);
		if(ObjectUtil.isNotEmpty(param.getAuditUid())){
			UserInfoModel auditUser = userApiService.getByStaffNo(param.getAuditUid());
			inspectionPlan.setAuditUid(param.getAuditUid());
			inspectionPlan.setAuditUname(auditUser.getUserName());
			inspectionPlan.setAuditStaffid(auditUser.getStaffid());
		}
		if(save(inspectionPlan)){
			return processMaterial(inspectionPlan.getId(),param.getMaterialParams());
		}
		return false;
	}

	/**
	 * 编辑巡检计划管理信息.
	 * @Param param 巡检计划管理信息
	 * @Return 编辑巡检计划管理是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean edit(InspectionPlanParam param) {
		InspectionPlan inspectionPlan = this.getById(param.getId());
		AssertUtils.notNull(inspectionPlan, SystemResultCode.RESULT_DATA_NONE.message());
		BeanUtils.copyProperties(param,inspectionPlan);
		if(updateById(inspectionPlan)){
			//先删除关联的材料信息
			inspectionMaterialService.remove(inspectionPlan.getId());
			return processMaterial(inspectionPlan.getId(),param.getMaterialParams());
		}
		return false;
	}

	/**
	 * 删除巡检计划管理.
	 * @Param id 巡检计划管理标识
	 * @Return 删除巡检计划管理是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		InspectionPlan inspectionPlan = this.getById(id);
		AssertUtils.notNull(inspectionPlan, SystemResultCode.RESULT_DATA_NONE.message());
		inspectionPlan.setDeleted(Delete.DELETED.getKey());
		return this.updateById(inspectionPlan);
	}

	@Override
	public Boolean statusEdit(InspectionPointStatusParam param) {
		InspectionPlan inspectionPlan = this.getById(param.getId());
		AssertUtils.notNull(inspectionPlan, SystemResultCode.RESULT_DATA_NONE.message());
		inspectionPlan.setStatus(param.getStatus());
		return updateById(inspectionPlan);
	}


	@Override
	public List<InspectionPointModel> findPointList(Long planId) {
		InspectionPlan inspectionPlan = this.getById(planId);
		AssertUtils.notNull(inspectionPlan, SystemResultCode.RESULT_DATA_NONE.message());
		if(inspectionPlan.getDeleted().equals(Delete.DELETED.getKey())){
			return Collections.emptyList();
		}
		return pointHandle(inspectionPlan.getPointId());
	}

	@Override
	public IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param) {
		param.setSource(WorkOrderSourceEnum.INSPECTIONPLAN.getCode());
		return workOrderService.businessPage(param);
	}

	@Override
	public Boolean executePlan(Map<Long, String> scheduleMap) {
		List<InspectionPlan> plans = this.getBaseMapper().selectListBySql();
		if(CollectionUtil.isEmpty(plans)){
			return false;
		}
		Date currentDate = DateUtil.date();
        Map<Long, List<InspectionPlan>> planMap = plans.stream().collect(Collectors.groupingBy(InspectionPlan::getTenantId));
        for(Map.Entry<Long,List<InspectionPlan>> entry : planMap.entrySet()){
            //获取工单数量
            Long number = workOrderService.queryWorkOrderCount(WorkOrderSourceEnum.INSPECTIONPLAN.getCode(), entry.getKey());
            for (InspectionPlan plan : entry.getValue()) {
                log.info("巡检计划信息{}", JSON.toJSONString(plan));
                // 判断当前日期是否满足计划的周期条件
                if (!PlanDateUtil.isDateValid(currentDate, plan.getPeriodType(), plan.getPeriodSign(), plan.getPeriodStartTime(), plan.getPlanPeriod())) {
                    log.info("巡检计划不符合生成工单规则,计划id：{}", plan.getId());
                    continue; // 如果不满足条件，跳过该计划
                }
                try {
                    number++;
                    WorkPlanModel planModel = BeanUtils.convertTo(plan, WorkPlanModel::new);
                    planModel.setPlanType(WorkOrderTypeEnum.INSPECTIONPLAN.getCode());
                    planModel.setPlanSource(WorkOrderSourceEnum.INSPECTIONPLAN.getCode());
                    planModel.setPlanCode(PlanDateUtil.getCode(WorkOrderCodePrefixConstant.XJ,  number));
                    planModel.setScheduleName(ObjectUtil.isNotEmpty(scheduleMap) ? scheduleMap.get(plan.getScheduleId()) : null);
                    //创建工单
                    WorkOrder workOrder = workOrderService.addPlanWordOrder(planModel, null);
                    //保存工单材料
                    addWorkMaterial(workOrder, planModel);
                    //保存工单任务
                    addWorkTask(workOrder, planModel);
                } catch (Exception e) {
                    log.error("巡检计划生成工单失败", e);
                }
            }
        }
		return true;
	}

    private void addWorkTask(WorkOrder workOrder, WorkPlanModel planModel){
        //维保任务
        List<InspectionPointModel> inspectionPoints = this.pointHandle(planModel.getPointId());
        if(CollectionUtil.isEmpty(inspectionPoints)) {
            return;
        }
        List<WorkTask> params = inspectionPoints.stream().map(inspectionPoint -> {
            WorkTask param = new WorkTask();
            param.setWorkId(workOrder.getId());
            param.setBusinessType(WorkTaskTypeEnum.INSPECTION_POINT.getCode());
            param.setBusinessId(inspectionPoint.getId());
            param.setName(inspectionPoint.getName());
            param.setCode(inspectionPoint.getCode());
            param.setSpaceId(inspectionPoint.getSpaceId());
            param.setSpaceFullPath(inspectionPoint.getSpaceName());
            param.setRemark(inspectionPoint.getRemark());
            param.setRedundancyOne(inspectionPoint.getDeviceId());
            param.setRedundancyTwo(inspectionPoint.getDeviceName());
            param.setTenantId(workOrder.getTenantId());
            param.setCreateTime(new Date());
            param.setCreateBy(workOrder.getCreateBy());
            param.setCreatorId(workOrder.getCreatorId());
            param.setUpdateBy(workOrder.getCreateBy());
            param.setUpdatorId(workOrder.getCreatorId());
            param.setUpdateTime(new Date());
            return param;
        }).collect(Collectors.toList());
        workTaskService.saveBatch(params);
    }

    private void addWorkMaterial(WorkOrder workOrder, WorkPlanModel planModel){
        //巡检材料
        List<InspectionMaterialModel> inspectionMaterials = this.materialHandle(planModel.getId());
        if(CollectionUtil.isEmpty(inspectionMaterials)){
            return;
        }
        List<Long> materialIds = inspectionMaterials.stream().map(InspectionMaterialModel::getMaterialId).collect(Collectors.toList());
        List<Material> materials = materialService.list(Wrappers.<Material>lambdaQuery().in(Material::getId, materialIds).eq(Material::getDeleted, Status.enabled.getKey()));
        Map<Long, Material> materialMap = CollectionUtil.isEmpty(materials) ? new HashMap<>(): materials.stream().collect(Collectors.toMap(Material::getId, material -> material));
        List<WorkMaterial> params = inspectionMaterials.stream().map(material -> {
            WorkMaterial param = new WorkMaterial();
            if(materialMap.get(material.getMaterialId()) != null){
                BeanUtils.copyProperties(materialMap.get(material.getMaterialId()), param);
            }
            param.setMaterialId(material.getMaterialId());
            param.setMaterialNum(material.getMaterialNum());
            param.setWorkId(workOrder.getId());
            param.setId(null);
            param.setTenantId(workOrder.getTenantId());
            param.setCreateTime(new Date());
            param.setCreateBy(workOrder.getCreateBy());
            param.setCreatorId(workOrder.getCreatorId());
            param.setUpdateBy(workOrder.getCreateBy());
            param.setUpdatorId(workOrder.getCreatorId());
            param.setUpdateTime(new Date());
            return param;
        }).collect(Collectors.toList());
        workMaterialService.saveBatch(params);
    }

	/**
	 * 保存巡检计划管理关联的材料信息
	 * @param id 巡检计划管理标识
	 * @param materials 材料信息
	 * @return 保存是否成功
	 */
	private boolean processMaterial(Long id, List<InspectionMaterialParam> materials){
		if(!CollectionUtils.isEmpty(materials)){
			materials.forEach(item -> item.setInspectionId(id));
			return inspectionMaterialService.addBatch(materials);
		}
		return true;
	}

	/**
	 * 处理巡检点信息
	 * @param pointIdStr 巡检点标识字符串
	 * @return 巡检点信息
	 */
	private List<InspectionPointModel> pointHandle(String pointIdStr){
		if(StringUtils.isEmpty(pointIdStr)){
			return Collections.emptyList();
		}
		//查询巡检点集合
		String[] pointIdArr = pointIdStr.split(",");
		Set<Long> pointIds = Arrays.stream(pointIdArr).map(Long::parseLong).collect(Collectors.toSet());
		InspectionPointListParam param = new InspectionPointListParam();
		param.setIds(new ArrayList<>(pointIds));
		List<InspectionPointModel> points = inspectionPointService.list(param);
		//未删除的巡检点id
		Set<Long> existPointIds = points.stream().map(InspectionPointModel::getId).collect(Collectors.toSet());
		List<InspectionPointModel> list = points.stream().map(point-> BeanUtils.convertTo(point, InspectionPointModel::new)).collect(Collectors.toList());
		//已经被删除的巡检点只返回基本字段
		pointIds.stream().filter(pointId->!existPointIds.contains(pointId)).forEach(pointId->{
			InspectionPointModel point = new InspectionPointModel();
			point.setId(pointId);
			point.setDeleted(Status.disabled.getKey());
			list.add(point);
		});
		return list;
	}

	/**
	 * 处理关联的材料信息
	 * @param id 巡检计划管理标识
	 */
	private List<InspectionMaterialModel> materialHandle(Long id){
		InspectionMaterialListParam param = new InspectionMaterialListParam();
		param.setInspectionId(id);
		return inspectionMaterialService.list(param);
	}

	/**
	 * 批量转换返回模型
	 * @param plans 巡检计划管理模型列表
	 * @return 巡检计划管理模型列表
	 */
	private List<InspectionPlanListModel> convert(List<InspectionPlan> plans){
		if(CollectionUtil.isEmpty(plans)){
			return Collections.emptyList();
		}
		Date currentDate = DateUtil.date();
		//物业分组信息
		List<Long> scheduleIds = plans.stream().map(InspectionPlan::getScheduleId).collect(Collectors.toList());
		Map<Long, String> scheduleMap =propertyScheduleService.getScheduleNameMap(scheduleIds);
		List<Long> planIds = plans.stream().map(InspectionPlan::getId).collect(Collectors.toList());
		List<WorkOrder> workOrders = workOrderService.list(Wrappers.<WorkOrder>lambdaQuery().ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())
				.eq(WorkOrder::getSource, WorkOrderSourceEnum.INSPECTIONPLAN.getCode())
				.in(WorkOrder::getBusinessId, planIds));
		Set<Long> businessIds = CollectionUtil.isEmpty(workOrders) ? Collections.emptySet() : workOrders.stream().map(WorkOrder::getBusinessId).collect(Collectors.toSet());
		//数据组装
		return plans.stream().map(plan -> {
			InspectionPlanListModel model = BeanUtils.convertTo(plan, InspectionPlanListModel::new);
			if(StringUtils.isNotEmpty(model.getPointId())){
				model.setPointCount(model.getPointId().split(",").length);
			}
			if(ObjectUtil.isNotEmpty(scheduleMap.get(model.getScheduleId()))){
				model.setScheduleName(scheduleMap.get(model.getScheduleId()));
			}
			if(Integer.valueOf(Status.enabled.getKey()).equals(model.getStatus())){
				model.setNextTime(PlanDateUtil.getNextDate(currentDate, model.getPeriodType(), model.getPeriodSign(), model.getPeriodStartTime(), model.getPlanPeriod()));
			}
			if (businessIds.contains(model.getId())){
				model.setWorkOrderFlag(true);
			}
			return model;
		}).collect(Collectors.toList());
	}

	private LambdaQueryWrapper<InspectionPlan> buildQuery(InspectionPlanListParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		return Wrappers.<InspectionPlan>lambdaQuery().like(ObjectUtil.isNotEmpty(param.getPlanName()), InspectionPlan::getPlanName, param.getPlanName())
				.eq(param.getPlanPeriod() != null, InspectionPlan::getPlanPeriod, param.getPlanPeriod())
		 		.eq(param.getScheduleId() != null, InspectionPlan::getScheduleId, param.getScheduleId())
				.eq(param.getDispatchType() != null, InspectionPlan::getDispatchType, param.getDispatchType())
				.eq(param.getStatus() != null, InspectionPlan::getStatus, param.getStatus())
				.eq(tenantId != null,InspectionPlan::getTenantId, tenantId)
				.eq(InspectionPlan::getDeleted, Status.enabled.getKey())
				.orderByDesc(InspectionPlan::getCreateTime);
	}
}