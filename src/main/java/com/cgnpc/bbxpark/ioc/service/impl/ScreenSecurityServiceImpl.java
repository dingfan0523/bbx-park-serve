package com.cgnpc.bbxpark.ioc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.constant.AlarmInfoConstant;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.enums.WorkOrderSourceEnum;
import com.cgnpc.bbxpark.common.enums.WorkOrderStatusEnum;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.*;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.mapper.*;
import com.cgnpc.bbxpark.device.service.IDeviceGroupRelService;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.AbnormalInspectionPointPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.AbnormalPatrolPointPageParam;
import com.cgnpc.bbxpark.ioc.service.IScreenSecurityService;
import com.cgnpc.bbxpark.property.domain.PatrolRoute;
import com.cgnpc.bbxpark.property.mapper.PatrolRouteRepository;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.service.IConfigInfoService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderRoman;
import com.cgnpc.bbxpark.workorder.domain.WorkTask;
import com.cgnpc.bbxpark.workorder.domain.WorkTaskItem;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRomanRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkTaskItemRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScreenSecurityServiceImpl implements IScreenSecurityService {
    @Autowired
    private AlarmInfoRepository alarmInfoRepository;
    @Autowired
    private AlarmDeviceRepository alarmDeviceRepository;
    @Autowired
    private IocDeviceRepository iocDeviceRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private DeviceGroupRelRepository deviceGroupRelRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkTaskRepository workTaskRepository;
    @Autowired
    private WorkTaskItemRepository workTaskItemRepository;
    @Autowired
    private WorkOrderRomanRepository workOrderRomanRepository;
    @Autowired
    private PatrolRouteRepository patrolRouteRepository;
    @Autowired
    private IDeviceGroupRelService deviceGroupRelService;
    @Autowired
    private IConfigInfoService configInfoService;
    private String deviceGroup = "ZHAF";

    @Override
    public SecurityOverviewModel getSecurityOverview() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //安防设备集合
        List<IocDevice> devices = iocDeviceRepository.findByGroup(deviceGroup, tenantId);
        List<Long> deviceIds = devices.stream().map(IocDevice::getId).collect(Collectors.toList());
        //离线设备数量
        long offlineCount = devices.stream().filter(d -> Status.disabled.getKey().equals(d.getIotDeviceStatus())).count();
        //安防设备告警总数
        Integer alarmTotal = alarmDeviceRepository.selectCount(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getDeviceId, deviceIds).eq(AlarmDevice::getTenantId, tenantId).select(AlarmDevice::getAlarmId));
        //安防设备未完成告警数量
        Long noCompleteAlarmTotal = countNoCompleteAlarm(deviceIds);
        //未完成的工单id集合,巡更巡检点都是统计已完成的工单,所以需要把其他状态的排除掉，剩下的才是已完成的
        List<Long> noCompleteOrderIds = getAuditingWorkOrderIds(WorkOrderSourceEnum.PATROLPLAN.getCode(), tenantId);
        //巡更点异常次数
        Integer abnormalCount = workTaskItemRepository.selectCount(Wrappers.<WorkTaskItem>lambdaQuery().eq(WorkTaskItem::getBusinessType, 2).eq(WorkTaskItem::getErrorStatus, Status.enabled.getKey())
                .notIn(CollectionUtils.isNotEmpty(noCompleteOrderIds),WorkTaskItem::getWorkId,noCompleteOrderIds).eq(WorkTaskItem::getTenantId, tenantId).select(WorkTaskItem::getId));
        //全部巡更点次数
        Integer total = workTaskItemRepository.selectCount(Wrappers.<WorkTaskItem>lambdaQuery().eq(WorkTaskItem::getBusinessType, 2)
                .notIn(CollectionUtils.isNotEmpty(noCompleteOrderIds),WorkTaskItem::getWorkId,noCompleteOrderIds).eq(WorkTaskItem::getTenantId, tenantId).select(WorkTaskItem::getId));
        //园区总人数
        ConfigInfoModel configInfo = configInfoService.getByCodeDetail(Constant.PARK_TOTAL_PEOPLE);
        Integer people = ObjectUtil.isEmpty(configInfo) ? 0 : Integer.parseInt(configInfo.getValue());
        return SecurityOverviewModel.builder().score(100 - Math.min(30, noCompleteAlarmTotal) - Math.min(30, offlineCount) - Math.min(30, abnormalCount * 100 / total))
                .alarmCloseRate(new BigDecimal((alarmTotal - noCompleteAlarmTotal) * 100).divide(new BigDecimal(alarmTotal), 2, RoundingMode.HALF_UP))
                .onlineRate(new BigDecimal(100).subtract(new BigDecimal(offlineCount * 100).divide(new BigDecimal(deviceIds.size()), 2, RoundingMode.HALF_UP)))
                .offlineDeviceCount(offlineCount).yesterdayEnterCount(0).peopleCount(people).build();
    }

    @Override
    public PatrolExecOverviewModel getPatrolExecOverview() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //全部巡更工单数量
        Integer allCount = workOrderRepository.selectCount(Wrappers.<WorkOrder>lambdaQuery().eq(WorkOrder::getSource, WorkOrderSourceEnum.PATROLPLAN.getCode())
                .eq(WorkOrder::getTenantId, tenantId).eq(WorkOrder::getDeleted, Delete.NORMAL.getKey()));
        //已完成巡更工单数量
        Integer completedCount = workOrderRepository.selectCount(Wrappers.<WorkOrder>lambdaQuery().eq(WorkOrder::getSource, WorkOrderSourceEnum.PATROLPLAN.getCode())
                .eq(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode()).eq(WorkOrder::getTenantId, tenantId).eq(WorkOrder::getDeleted, Delete.NORMAL.getKey()));
        //未完成的工单id集合,巡更巡检点都是统计已完成的工单,所以需要把其他状态的排除掉，剩下的才是已完成的
        List<Long> noCompleteOrderIds = getAuditingWorkOrderIds(WorkOrderSourceEnum.PATROLPLAN.getCode(), tenantId);
        //已完成工单巡更点总次数
        Integer pointCount = workTaskItemRepository.selectCount(Wrappers.<WorkTaskItem>lambdaQuery().eq(WorkTaskItem::getBusinessType,2)
                .notIn(CollectionUtils.isNotEmpty(noCompleteOrderIds), WorkTaskItem::getWorkId, noCompleteOrderIds).eq(WorkTaskItem::getTenantId, tenantId));
        //已完成工单巡更点异常次数
        Integer abnormalPointCount = workTaskItemRepository.selectCount(Wrappers.<WorkTaskItem>lambdaQuery().eq(WorkTaskItem::getErrorStatus, Status.enabled.getKey())
                .eq(WorkTaskItem::getBusinessType,2)
                .notIn(CollectionUtils.isNotEmpty(noCompleteOrderIds), WorkTaskItem::getWorkId, noCompleteOrderIds).eq(WorkTaskItem::getTenantId, tenantId));
        PatrolExecOverviewModel model = workTaskRepository.getPatrolExecOverview(3, tenantId);
        model.setWorkOrderFinishRate(allCount <= 0 ? BigDecimal.ZERO : new BigDecimal(completedCount * 100).divide(new BigDecimal(allCount), 2, RoundingMode.HALF_UP));
        model.setPatrolAbnormalRate(pointCount <= 0 ? BigDecimal.ZERO : new BigDecimal(abnormalPointCount * 100).divide(new BigDecimal(pointCount), 2, RoundingMode.HALF_UP));
        model.setEstDuration(model.getEstDuration().divide(new BigDecimal(60),2,RoundingMode.HALF_UP));
        return model;
    }

    @Override
    public List<PatrolRouteTypeStatModel> getPatrolRouteTypeStat() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //路线类型
        Integer[] types = {1,2,3,4};
        //获取不同路线的巡更点正常数和异常数
        List<PatrolRouteTypeStatModel> list = workTaskRepository.getPatrolRouteTypeStat(tenantId);
        Map<Long,PatrolRouteTypeStatModel> statMap = list.stream().collect(Collectors.toMap(PatrolRouteTypeStatModel::getType,p->p,(v1,v2)->v2));
        List<PatrolRoute> routes = patrolRouteRepository.selectList(Wrappers.<PatrolRoute>lambdaQuery().eq(PatrolRoute::getStatus,Status.enabled.getKey()).eq(PatrolRoute::getTenantId,tenantId));

        Map<Integer,List<Long>> typeMap = routes.stream().collect(Collectors.groupingBy(PatrolRoute::getType,Collectors.mapping(PatrolRoute::getId,Collectors.toList())));
        return Arrays.stream(types).map(type->{
            PatrolRouteTypeStatModel model = new PatrolRouteTypeStatModel();
            model.setType(Long.valueOf(type));
            List<Long> routeIds = typeMap.getOrDefault(type,Collections.emptyList());
            routeIds.stream().filter(statMap::containsKey).forEach(id->{
                model.setNormalCount(model.getNormalCount() + statMap.get(id).getNormalCount());
                model.setAbnormalCount(model.getAbnormalCount() + statMap.get(id).getAbnormalCount());
            });
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PatrolRouteListItemModel> getPatrolList() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //获取不同路线的巡更点正常数和异常数
        List<PatrolRouteTypeStatModel> list = workTaskRepository.getPatrolRouteTypeStat(tenantId);
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List<Long> routeIds = list.stream().map(PatrolRouteTypeStatModel::getType).collect(Collectors.toList());
        List<PatrolRoute> routes = patrolRouteRepository.selectList(Wrappers.<PatrolRoute>lambdaQuery().in(PatrolRoute::getId,routeIds));
        Map<Long,PatrolRoute> routeMap = routes.stream().collect(Collectors.toMap(PatrolRoute::getId, Function.identity(),(v1,v2)->v2));
        return list.stream().map(s->{
            PatrolRouteListItemModel model = new PatrolRouteListItemModel();
            model.setId(s.getType());
            model.setName(routeMap.containsKey(s.getType()) ? routeMap.get(s.getType()).getName() : null);
            model.setPointAbnormalCount(s.getAbnormalCount());
            model.setPointTotalCount(s.getAbnormalCount() + s.getNormalCount());
            int abnormalRate = model.getPointAbnormalCount() == 0 || model.getPointTotalCount() == 0 ? 0 : model.getPointAbnormalCount() * 100 / model.getPointTotalCount();
            model.setSuggestion(abnormalRate >=50 ? "高发风险，建议增加巡更频次" : abnormalRate > 0 ? "正常巡更" : "无风险，可降低巡更频次");
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AlarmTypeDistributionModel> getAlarmTypeDistribution() {
        return alarmInfoRepository.getAlarmTypeDistribution(deviceGroup,WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<AlarmLevelDistributionModel> getAlarmLevelDistribution() {
        return alarmInfoRepository.getAlarmLevelDistribution(deviceGroup,WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<InspectionFaultRankModel> getInspectionFaultTop10() {
        return workTaskRepository.getInspectionFaultTop10(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<PersonPassTrendModel> getPersonPassTrend7d() {
        return Collections.emptyList();
    }

    @Override
    public IPage<AbnormalPatrolPointModel> pageAbnormalPatrolPoint(AbnormalPatrolPointPageParam param) {
        //未完成的工单id集合,巡更巡检点都是统计已完成的工单,所以需要把其他状态的排除掉，剩下的才是已完成的
        List<Long> noCompleteOrderIds = getAuditingWorkOrderIds(WorkOrderSourceEnum.PATROLPLAN.getCode(), WebFrameworkUtils.getHeaderTenantId());

        IPage<WorkTaskItem> page = workTaskItemRepository.selectPage(param.getPage(),Wrappers.<WorkTaskItem>lambdaQuery()
                .eq(WorkTaskItem::getBusinessType,2).eq(WorkTaskItem::getErrorStatus,Status.enabled.getKey())
                .like(StringUtils.isNotEmpty(param.getName()),WorkTaskItem::getName,param.getName())
                .eq(param.getType() != null,WorkTaskItem::getType,param.getType())
                .notIn(CollectionUtils.isNotEmpty(noCompleteOrderIds),WorkTaskItem::getWorkId,noCompleteOrderIds)
                .eq(WorkTaskItem::getTenantId,WebFrameworkUtils.getHeaderTenantId()).eq(WorkTaskItem::getDeleted,Delete.NORMAL.getKey()));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),page.getSize());
        }
        Set<Long> workOrderIds = page.getRecords().stream().map(WorkTaskItem::getWorkId).collect(Collectors.toSet());
        //根据工单id查询任务列表，以便获取巡更路线信息
        List<WorkTask> tasks = workTaskRepository.selectList(Wrappers.<WorkTask>lambdaQuery().in(WorkTask::getWorkId,workOrderIds));
        Map<Integer,WorkTask> groupMap = tasks.stream().collect(Collectors.toMap(WorkTask::getTaskGroup,Function.identity(),(v1,v2)->v2));
        //根据工单id查询已完成节点列表，以便获取实际的巡更时间
        List<WorkOrderRoman> romens = workOrderRomanRepository.selectList(Wrappers.<WorkOrderRoman>lambdaQuery().in(WorkOrderRoman::getWorkOrderId,workOrderIds)
                .eq(WorkOrderRoman::getRomanStatus,WorkOrderStatusEnum.COMPLETED.getCode()).select(WorkOrderRoman::getWorkOrderId,WorkOrderRoman::getCreateTime));
        Map<Long,Date> romenMap = romens.stream().collect(Collectors.toMap(WorkOrderRoman::getWorkOrderId,WorkOrderRoman::getCreateTime,(v1,v2)->v2));

        List<AbnormalPatrolPointModel> list = page.getRecords().stream().map(i->{
            AbnormalPatrolPointModel model = BeanUtils.convertTo(i,AbnormalPatrolPointModel::new);
            WorkTask task = groupMap.getOrDefault(i.getTaskGroup(),null);
            model.setSpaceName(i.getSpaceFullPath());
            model.setTime(romenMap.getOrDefault(i.getWorkId(),null));
            model.setRouteId(task == null ? null : task.getBusinessId());
            model.setRouteName(task == null ? null : task.getName());
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(page,list);
    }

    @Override
    public IPage<AbnormalInspectionPointModel> pageAbnormalInspectionPoint(AbnormalInspectionPointPageParam param) {
        //未完成的工单id集合,巡更巡检点都是统计已完成的工单,所以需要把其他状态的排除掉，剩下的才是已完成的
        List<Long> noCompleteOrderIds = getAuditingWorkOrderIds(WorkOrderSourceEnum.PATROLPLAN.getCode(), WebFrameworkUtils.getHeaderTenantId());

        IPage<WorkTask> page = workTaskRepository.selectPage(param.getPage(),Wrappers.<WorkTask>lambdaQuery()
                .eq(WorkTask::getBusinessType,2).eq(WorkTask::getErrorStatus,Status.enabled.getKey()).eq(WorkTask::getBusinessId,param.getId())
                .notIn(CollectionUtils.isNotEmpty(noCompleteOrderIds),WorkTask::getWorkId,noCompleteOrderIds)
                .eq(WorkTask::getTenantId,WebFrameworkUtils.getHeaderTenantId()).eq(WorkTask::getDeleted,Delete.NORMAL.getKey()));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),page.getSize());
        }
        Set<Long> workOrderIds = page.getRecords().stream().map(WorkTask::getWorkId).collect(Collectors.toSet());
        List<WorkOrder> workOrders = workOrderRepository.selectList(Wrappers.<WorkOrder>lambdaQuery().in(WorkOrder::getId,workOrderIds).select(WorkOrder::getId,WorkOrder::getCode));
        Map<Long,String> orderMap = workOrders.stream().collect(Collectors.toMap(WorkOrder::getId,WorkOrder::getCode));
        //根据工单id查询已完成节点列表，以便获取实际的巡更时间
        List<WorkOrderRoman> romens = workOrderRomanRepository.selectList(Wrappers.<WorkOrderRoman>lambdaQuery().in(WorkOrderRoman::getWorkOrderId,workOrderIds)
                .eq(WorkOrderRoman::getRomanStatus,WorkOrderStatusEnum.COMPLETED.getCode()).select(WorkOrderRoman::getWorkOrderId,WorkOrderRoman::getCreateTime));
        Map<Long,Date> romenMap = romens.stream().collect(Collectors.toMap(WorkOrderRoman::getWorkOrderId,WorkOrderRoman::getCreateTime,(v1,v2)->v2));

        List<AbnormalInspectionPointModel> list = page.getRecords().stream().map(i->{
            AbnormalInspectionPointModel model = BeanUtils.convertTo(i,AbnormalInspectionPointModel::new);
            model.setSpaceName(i.getSpaceFullPath());
            model.setTime(romenMap.getOrDefault(i.getWorkId(),null));
            model.setWorkOrderId(i.getWorkId());
            model.setWorkOrderCode(orderMap.getOrDefault(i.getWorkId(),""));
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(page,list);
    }

    /**
     * 获取待审核的工单id集合
     */
    private List<Long> getAuditingWorkOrderIds(String source, Long tenantId) {
        List<WorkOrder> orders = workOrderRepository.selectList(Wrappers.<WorkOrder>lambdaQuery().eq(StringUtils.isNotEmpty(source), WorkOrder::getSource, source)
                .ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode()).eq(WorkOrder::getTenantId, tenantId)
                .eq(WorkOrder::getDeleted, Delete.NORMAL.getKey()).select(WorkOrder::getId));
        return orders.stream().map(WorkOrder::getId).collect(Collectors.toList());
    }

    /**
     * 查询安防设备未完成告警数量
     */
    private Long countNoCompleteAlarm(List<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return 0L;
        }
        List<AlarmInfo> alarmInfos = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().select(AlarmInfo::getId).ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3).eq(AlarmInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        if (CollectionUtils.isEmpty(alarmInfos)) {
            return 0L;
        }
        List<Long> alarmIds = alarmInfos.stream().map(AlarmInfo::getId).collect(Collectors.toList());
        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getAlarmId, alarmIds));
        return alarmDevices.stream().filter(a -> deviceIds.contains(a.getDeviceId())).map(AlarmDevice::getAlarmId).distinct().count();
    }
}
