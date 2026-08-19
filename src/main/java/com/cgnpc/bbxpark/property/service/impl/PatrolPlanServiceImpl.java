package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.cgnpc.bbxpark.property.domain.MaintainPlan;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.domain.PatrolPlan;
import com.cgnpc.bbxpark.property.domain.PatrolRoute;
import com.cgnpc.bbxpark.property.dto.model.*;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.mapper.PatrolPlanRepository;
import com.cgnpc.bbxpark.property.service.*;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.workorder.domain.WorkMaterial;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkTask;
import com.cgnpc.bbxpark.workorder.domain.WorkTaskItem;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkMaterialParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskItemParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskParam;
import com.cgnpc.bbxpark.workorder.service.IWorkMaterialService;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import com.cgnpc.bbxpark.workorder.service.IWorkTaskItemService;
import com.cgnpc.bbxpark.workorder.service.IWorkTaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡更计划管理服务实现
 */
@Slf4j
@Service
public class PatrolPlanServiceImpl extends ServiceImpl<PatrolPlanRepository, PatrolPlan> implements IPatrolPlanService {
    @Resource
	private IPatrolRouteService patrolRouteService;
	@Resource
	private IPatrolMaterialService patrolMaterialService;
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

    @Resource
    private IWorkTaskItemService workTaskItemService;

    @Resource
    private IPatrolPointService patrolPointService;

	/**
	 * 根据巡更计划管理标识获得巡更计划管理详情信息.
	 * @Param [id] 巡更计划管理标识
	 * @Return 巡更计划管理详情信息
	 */
	@Override
	public PatrolPlanModel detail(Long id) {
		PatrolPlan patrolPlan = this.getById(id);
		AssertUtils.notNull(patrolPlan, SystemResultCode.RESULT_DATA_NONE.message());
		PatrolPlanModel model = BeanUtils.convertTo(patrolPlan, PatrolPlanModel::new);
		//巡更材料
		model.setMaterialModels(materialHandle(id));
		return model;
	}

	/**
	 * 获取巡更计划管理列表(分页).
	 * @Param param 巡更计划管理查询条件
	 * @Return 巡更计划管理信息列表（分页）
	 */
	@Override
	public IPage<PatrolPlanListModel> page(PatrolPlanPageParam param) {
		IPage<PatrolPlan> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, PatrolPlanListParam::new)));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result, convert(result.getRecords()));
	}

	/**
	 * 获取巡更计划管理列表.
	 * @Param param 巡更计划管理查询条件
	 * @Return 巡更计划管理信息列表
	 */
	@Override
	@SneakyThrows
	public List<PatrolPlanListModel> list(PatrolPlanListParam param) {
		List<PatrolPlan> plans = this.list(buildQuery(param));
		return BeanUtils.convertListTo(plans, PatrolPlanListModel::new);
	}

	/**
	 * 新增巡更计划管理.
	 * @Param param 巡更计划管理信息
	 * @Return 新增巡更计划管理是否成功
	 */
	@Override
	public Boolean add(PatrolPlanParam param) {
		PatrolPlan patrolPlan = BeanUtils.convertTo(param, PatrolPlan::new);
		//创建人
		UserInfoModel userInfo = userApiService.getCurrentUserInfo();
        patrolPlan.setCreateBy(userInfo.getUserName());
		patrolPlan.setId(null);
		if(ObjectUtil.isNotEmpty(param.getAuditUid())){
			UserInfoModel auditUser = userApiService.getByStaffNo(param.getAuditUid());
			patrolPlan.setAuditUid(param.getAuditUid());
			patrolPlan.setAuditUname(auditUser.getUserName());
			patrolPlan.setAuditStaffid(auditUser.getStaffid());
		}
		if(save(patrolPlan)){
			return processMaterial(patrolPlan.getId(),param.getMaterialParams());
		}
		return false;
	}

	/**
	 * 编辑巡更计划管理信息.
	 * @Param param 巡更计划管理信息
	 * @Return 编辑巡更计划管理是否成功
	 */
	@Override
	public Boolean edit(Long id, PatrolPlanParam param) {
		PatrolPlan patrolPlan = this.getById(param.getId());
		AssertUtils.notNull(patrolPlan, SystemResultCode.RESULT_DATA_NONE.message());
		BeanUtils.copyProperties(param,patrolPlan);
		if(updateById(patrolPlan)){
			//先删除关联的材料信息
			patrolMaterialService.remove(patrolPlan.getId());
			return processMaterial(patrolPlan.getId(),param.getMaterialParams());
		}
		return false;
	}

	/**
	 * 删除巡更计划管理.
	 * @Param id 巡更计划管理标识
	 * @Return 删除巡更计划管理是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		PatrolPlan patrolPlan = this.getById(id);
		AssertUtils.notNull(patrolPlan, SystemResultCode.RESULT_DATA_NONE.message());
		patrolPlan.setDeleted(Delete.DELETED.getKey());
		return updateById(patrolPlan);
	}

	@Override
	public Boolean statusEdit(InspectionPointStatusParam param) {
		PatrolPlan patrolPlan = this.getById(param.getId());
		AssertUtils.notNull(patrolPlan, SystemResultCode.RESULT_DATA_NONE);
		patrolPlan.setStatus(param.getStatus());
		return updateById(patrolPlan);
	}

	@Override
	public List<SimplePatrolRouteModel> findRouteList(Long planId) {
		PatrolPlan plan = this.getById(planId);
		AssertUtils.notNull(plan, SystemResultCode.RESULT_DATA_NONE.message());
		if(plan.getDeleted().equals(Delete.DELETED.getKey())){
			return Collections.emptyList();
		}
		return routeHandle(plan.getRouteId());
	}

	@Override
	public IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param) {
		param.setSource(WorkOrderSourceEnum.PATROLPLAN.getCode());
		return workOrderService.businessPage(param);
	}

	@Override
	public Boolean executePlan(Map<Long, String> scheduleMap) {
		List<PatrolPlan> plans = this.getBaseMapper().selectListBySql();
		if(CollectionUtil.isEmpty(plans)){
			return false;
		}
		Date currentDate = DateUtil.date();
        Map<Long, List<PatrolPlan>> planMap = plans.stream().collect(Collectors.groupingBy(PatrolPlan::getTenantId));
        for(Map.Entry<Long,List<PatrolPlan>> entry : planMap.entrySet()){
            //获取工单数量
            Long number = workOrderService.queryWorkOrderCount(WorkOrderSourceEnum.PATROLPLAN.getCode(), entry.getKey());
            for (PatrolPlan plan : entry.getValue()) {
                log.info("巡更计划信息{}", JSON.toJSONString(plan));
                // 判断当前日期是否满足计划的周期条件
                if (!PlanDateUtil.isDateValid(currentDate, plan.getPeriodType(), plan.getPeriodSign(), plan.getPeriodStartTime(), plan.getPlanPeriod())) {
                    log.info("巡更计划不符合生成工单规则,计划id：{}", plan.getId());
                    continue; // 如果不满足条件，跳过该计划
                }
                try {
                    number++;
                    WorkPlanModel planModel = BeanUtils.convertTo(plan, WorkPlanModel::new);
                    planModel.setPlanType(WorkOrderTypeEnum.PATROLPLAN.getCode());
                    planModel.setPlanSource(WorkOrderSourceEnum.PATROLPLAN.getCode());
                    planModel.setPlanCode(PlanDateUtil.getCode(WorkOrderCodePrefixConstant.XG,  number));
                    planModel.setScheduleName(ObjectUtil.isNotEmpty(scheduleMap) ? scheduleMap.get(plan.getScheduleId()) : null);
                    //创建计划工单
                    WorkOrder workOrder = workOrderService.addPlanWordOrder(planModel, null);
                    //保存工单材料
                    addWorkMaterial(workOrder, planModel);
                    //保存工单任务
                    addWorkTask(workOrder, planModel);
                } catch (Exception e) {
                    log.error("巡更计划生成工单失败", e);
                }
            }
        }
		return true;
	}

    private void addWorkTask(WorkOrder workOrder, WorkPlanModel planModel){
        if(ObjectUtil.isEmpty(planModel.getRouteId())){
            return;
        }
        //巡更任务
        List<Long> routeIdList = Arrays.stream(planModel.getRouteId().split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<PatrolRoute> routeList = patrolRouteService.list(Wrappers.<PatrolRoute>lambdaQuery().in(PatrolRoute::getId, routeIdList).eq(PatrolRoute::getDeleted, Status.enabled.getKey()));
        Map<Long, PatrolRoute> routeMap = CollectionUtil.isEmpty(routeList) ? new HashMap<>():  routeList.stream().collect(Collectors.toMap(PatrolRoute::getId, route -> route));
        Map<Long, PatrolPointModel> pointMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(routeMap)){
            List<Long> pointIdList = routeList.stream().filter(route-> ObjectUtil.isNotEmpty(route.getPointId()))
                    .flatMap(route -> Arrays.stream(route.getPointId().split(",")).map(Long::parseLong))
                    .collect(Collectors.toList());
            PatrolPointListParam patrolRouteListParam = new PatrolPointListParam();
            patrolRouteListParam.setIds(pointIdList);
            List<PatrolPointModel> points = patrolPointService.list(patrolRouteListParam);
            // 创建一个Map以便根据ID快速查找巡更点
            pointMap = points.stream().collect(Collectors.toMap(PatrolPointModel::getId, point -> point));

        }
        List<WorkTask> params = new ArrayList<>();
        List<WorkTaskItem> itemParams = new ArrayList<>();
        int group = 0;
        for (Long routeId : routeIdList) {
            WorkTask param = new WorkTask();
            param.setWorkId(workOrder.getId());
            param.setBusinessType(WorkTaskTypeEnum.PATROL_ROUTE.getCode());
            param.setTaskGroup(group);
            param.setBusinessId(routeId);
            if(ObjectUtil.isNotEmpty(routeMap.get(routeId))) {
                PatrolRoute route = routeMap.get(routeId);
                param.setName(route.getName());
                param.setCategory(route.getType());
                param.setRemark(route.getRemark());
                param.setDistance(route.getDistance());
                param.setUseTime(route.getUseTime());
                if(ObjectUtil.isNotEmpty(route.getPointId())) {
                    List<Long> pointIds = Arrays.stream(route.getPointId().split(",")).map(Long::parseLong).collect(Collectors.toList());
                    List<WorkTaskItem> items = new ArrayList<>();
                    for (Long pointId : pointIds) {
                        PatrolPointModel point = pointMap.get(pointId);
                        WorkTaskItem item = new WorkTaskItem();
                        if (point != null) {
                            BeanUtil.copyProperties(point, item);
                            item.setSpaceFullPath(point.getSpaceName());
                        }
                        item.setWorkId(workOrder.getId());
                        item.setBusinessId(pointId);
                        item.setBusinessType(WorkTaskItemTypeEnum.PATROL_POINT.getCode());
                        item.setId(null);
                        item.setTaskGroup(group);
                        item.setTenantId(workOrder.getTenantId());
                        item.setCreateTime(new Date());
                        item.setCreateBy(workOrder.getCreateBy());
                        item.setCreatorId(workOrder.getCreatorId());
                        item.setUpdateBy(workOrder.getCreateBy());
                        item.setUpdatorId(workOrder.getCreatorId());
                        item.setUpdateTime(new Date());
                        items.add(item);
                    }
                    itemParams.addAll(items);
                }
            }
            param.setTenantId(workOrder.getTenantId());
            param.setCreateTime(new Date());
            param.setCreateBy(workOrder.getCreateBy());
            param.setCreatorId(workOrder.getCreatorId());
            param.setUpdateBy(workOrder.getCreateBy());
            param.setUpdatorId(workOrder.getCreatorId());
            param.setUpdateTime(new Date());
            params.add(param);
            group ++;
        }
        workTaskService.saveBatch(params);
        if(CollectionUtil.isNotEmpty(itemParams)){
            workTaskItemService.saveBatch(itemParams);
        }
    }

    private void addWorkMaterial(WorkOrder workOrder, WorkPlanModel planModel){
        //巡更材料
        List<PatrolMaterialModel> patrolMaterials = this.materialHandle(planModel.getId());
        if(CollectionUtil.isEmpty(patrolMaterials)){
            return;
        }
        List<Long> materialIds = patrolMaterials.stream().map(PatrolMaterialModel::getMaterialId).collect(Collectors.toList());
        List<Material> materials = materialService.list(Wrappers.<Material>lambdaQuery().in(Material::getId, materialIds).eq(Material::getDeleted, Status.enabled.getKey()));
        Map<Long, Material> materialMap = CollectionUtil.isEmpty(materials) ? new HashMap<>(): materials.stream().collect(Collectors.toMap(Material::getId, material -> material));
        List<WorkMaterial> params = patrolMaterials.stream().map(material -> {
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
	 * 保存巡更计划管理关联的材料信息
	 * @param id 巡更计划管理标识
	 * @param materials 材料信息
	 * @return 保存是否成功
	 */
	private boolean processMaterial(Long id, List<PatrolMaterialParam> materials){
		if(!CollectionUtils.isEmpty(materials)){
			materials.forEach(item -> item.setPatrolId(id));
			return patrolMaterialService.addBatch(materials);
		}
		return true;
	}

	/**
	 * 处理关联的材料信息
	 * @param id 巡更计划管理标识
	 */
	private List<PatrolMaterialModel> materialHandle(Long id){
		PatrolMaterialListParam param = new PatrolMaterialListParam();
		param.setPatrolId(id);
		return patrolMaterialService.list(param);
	}

	/**
	 * 处理巡更路线信息
	 * @param routeIdStr 巡更路线标识字符串
	 * @return 巡更路线信息
	 */
	private List<SimplePatrolRouteModel> routeHandle(String routeIdStr){
		if(StringUtils.isEmpty(routeIdStr)){
			return Collections.emptyList();
		}
		//查询巡检点集合
		String[] routeIdArr = routeIdStr.split(",");
		List<Long> routeIds = Arrays.stream(routeIdArr).map(Long::parseLong).collect(Collectors.toList());
		PatrolRouteListParam param = new PatrolRouteListParam();
		param.setIds(routeIds);
		List<PatrolRouteModel> routes = patrolRouteService.list(param);
		//未删除的巡更路线id
		Set<Long> existRouteIds = routes.stream().map(PatrolRouteModel::getId).collect(Collectors.toSet());
		List<SimplePatrolRouteModel> list = routes.stream().map(point-> BeanUtils.convertTo(point, SimplePatrolRouteModel::new)).collect(Collectors.toList());
		//已经被删除的巡检点只返回基本字段
		routeIds.stream().filter(pointId->!existRouteIds.contains(pointId)).forEach(routeId->{
			SimplePatrolRouteModel route = new SimplePatrolRouteModel();
			route.setId(routeId);
			route.setDeleted(Delete.DELETED.getKey());
			list.add(route);
		});
		return list;
	}

	/**
	 * 批量转换返回模型
	 * @param plans 巡更计划管理模型列表
	 * @return 巡更计划管理模型列表
	 */
	private List<PatrolPlanListModel> convert(List<PatrolPlan> plans){
		if(CollectionUtil.isEmpty(plans)){
			return Collections.emptyList();
		}
		Date currentDate = DateUtil.date();
		//巡更点信息
		List<Long> routeIdList = plans.stream().filter(plan -> ObjectUtil.isNotEmpty(plan.getRouteId()))
				.flatMap(plan -> Arrays.stream(plan.getRouteId().split(",")).map(Long::parseLong))
				.collect(Collectors.toList());
		
		List<PatrolRoute> routeList = Collections.emptyList();
		if (!routeIdList.isEmpty()) {
			routeList = patrolRouteService.list(Wrappers.<PatrolRoute>lambdaQuery().in(PatrolRoute::getId, routeIdList).eq(PatrolRoute::getDeleted, Delete.NORMAL.getKey()));
		}
		Map<Long, PatrolRoute> routeMap = routeList.stream().collect(Collectors.toMap(PatrolRoute::getId, route -> route));
		
		Map<Long, Integer> routePointCountMap = routeList.stream()
				.filter(route -> ObjectUtil.isNotEmpty(route.getPointId()))
				.collect(Collectors.toMap(PatrolRoute::getId, route -> route.getPointId().split(",").length));
		
		//物业分组信息
		List<Long> scheduleIds = plans.stream().map(PatrolPlan::getScheduleId).filter(Objects::nonNull).collect(Collectors.toList());
		
		Map<Long, String> scheduleMap = Collections.emptyMap();
		if (!scheduleIds.isEmpty()) {
			scheduleMap = propertyScheduleService.getScheduleNameMap(scheduleIds);
		}

		List<Long> planIds = plans.stream().map(PatrolPlan::getId).collect(Collectors.toList());
		List<WorkOrder> workOrders = workOrderService.list(Wrappers.<WorkOrder>lambdaQuery().ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())
				.eq(WorkOrder::getSource, WorkOrderSourceEnum.PATROLPLAN.getCode())
				.in(WorkOrder::getBusinessId, planIds));
		Set<Long> businessIds = CollectionUtil.isEmpty(workOrders) ? Collections.emptySet() : workOrders.stream().map(WorkOrder::getBusinessId).collect(Collectors.toSet());
		
		//数据组装
		Map<Long, String> finalScheduleMap = scheduleMap;
		return plans.stream().map(plan -> {
			PatrolPlanListModel model = BeanUtils.convertTo(plan, PatrolPlanListModel::new);
			if(StringUtils.isNotEmpty(model.getRouteId())){
				// 设置巡更点数量
				model.setPointCount(Arrays.stream(plan.getRouteId().split(","))
						.map(routeId -> routePointCountMap.getOrDefault(Long.parseLong(routeId), 0))
						.reduce(0, Integer::sum));
				
				// 设置路线名称
				String routeName = Arrays.stream(plan.getRouteId().split(",")).map(routeIdStr -> {
							long routeId = Long.parseLong(routeIdStr);
							PatrolRoute route = routeMap.get(routeId);
							return route != null ? route.getName() : String.valueOf(routeId);
						}).collect(Collectors.joining("、"));
				model.setRouteName(routeName);
			}
			if(ObjectUtil.isNotEmpty(finalScheduleMap.get(model.getScheduleId()))){
				model.setScheduleName(finalScheduleMap.get(model.getScheduleId()));
			}
			if(Status.enabled.getKey().equals(model.getStatus())){
				model.setNextTime(PlanDateUtil.getNextDate(currentDate, model.getPeriodType(), model.getPeriodSign(), 
						model.getPeriodStartTime(), model.getPlanPeriod()));
			}
			if (businessIds.contains(model.getId())){
				model.setWorkOrderFlag(true);
			}
			return model;
		}).collect(Collectors.toList());
	}

	private LambdaQueryWrapper<PatrolPlan> buildQuery(PatrolPlanListParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		return Wrappers.<PatrolPlan>lambdaQuery().like(ObjectUtil.isNotEmpty(param.getPlanName()), PatrolPlan::getPlanName, param.getPlanName())
				.eq(param.getPlanPeriod() != null, PatrolPlan::getPlanPeriod, param.getPlanPeriod())
				.eq(param.getScheduleId() != null, PatrolPlan::getScheduleId, param.getScheduleId())
				.eq(param.getDispatchType() != null, PatrolPlan::getDispatchType, param.getDispatchType())
				.eq(param.getStatus() != null, PatrolPlan::getStatus, param.getStatus())
				.eq(tenantId != null,PatrolPlan::getTenantId, tenantId)
				.eq(PatrolPlan::getDeleted, Delete.NORMAL.getKey())
				.orderByDesc(PatrolPlan::getCreateTime);
	}
}