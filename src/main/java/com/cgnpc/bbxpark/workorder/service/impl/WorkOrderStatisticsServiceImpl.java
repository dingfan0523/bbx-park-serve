package com.cgnpc.bbxpark.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cgnpc.bbxpark.settings.domain.AttentionManage;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.common.enums.WorkOrderHandleResultEnum;
import com.cgnpc.bbxpark.common.enums.WorkOrderSourceEnum;
import com.cgnpc.bbxpark.common.enums.WorkOrderStatusEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.ExcelExportUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.TenantDomain;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderStatisticsExportModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderStatisticsModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderStatisticsParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderDeviceRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderStatisticsService;
import com.cgnpc.cud.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @create zhaoshuo
 * @time 2025/4/15
 * @desc 物业工单统计服务实现
 */
@Service
public class WorkOrderStatisticsServiceImpl implements IWorkOrderStatisticsService {
    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private IAttentionManageService attentionManageService;

    @Autowired
    private WorkOrderDeviceRepository workOrderDeviceRepository;

//    @Autowired
//    private TenantInfoFeignClient tenantInfoFeignClient;

    @Autowired
    private ITenantInfoService tenantInfoService;

    @Override
    public WorkOrderStatisticsModel getWorkOrderStatistics(WorkOrderStatisticsParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();

        WorkOrderStatisticsModel model = new WorkOrderStatisticsModel();
        //时间范围内的工单
        List<WorkOrder> workOrders = workOrderRepository.selectList(new LambdaQueryWrapper<WorkOrder>().between(WorkOrder::getCreateTime, param.getStartTime(), param.getEndTime()).eq(WorkOrder::getTenantId, tenantId));
        //遗留工单（查询开始时间之前的未完成的工单）
        List<WorkOrder> leftoverWorkOrders = workOrderRepository.selectList(new LambdaQueryWrapper<WorkOrder>().lt(WorkOrder::getCreateTime, param.getStartTime()).eq(WorkOrder::getTenantId, tenantId).ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode()));
        model.setLeftoverWorkOrderCount(leftoverWorkOrders.size());
        // 新产生处理工单数量
        model.setNewWorkOrderCount(workOrders.size());
        // 合计工单数量=新产生处理工单数量+遗留工单数量
        model.setTotalWorkOrderCount(workOrders.size() + leftoverWorkOrders.size());
        //从查出来的数据中获取工单状态是已完成的数据
        List<WorkOrder> completeWorkOrders = workOrders.stream().filter(workOrder -> workOrder.getStatus() == WorkOrderStatusEnum.COMPLETED.getCode()).collect(Collectors.toList());
        model.setCompleteWorkOrderCount(completeWorkOrders.size());
        //未完成的工单数量=新产生处理工单数量-已完成工单数量+遗留工单数量
        model.setIncompleteWorkOrderCount(model.getNewWorkOrderCount() - model.getCompleteWorkOrderCount() + model.getLeftoverWorkOrderCount());

        //筛选出workOrders中所有的异常结束工单并根据工单类型进行分类
        List<WorkOrder> abnormalWorkOrders = workOrders.stream().filter(workOrder -> workOrder.getHandleResult() == WorkOrderHandleResultEnum.TYPE_2.getCode()).collect(Collectors.toList());
        //异常结束报事报修工单
        model.setAbnormalPersonWorkOrder(abnormalWorkOrders.stream().filter(workOrder -> workOrder.getSource().equals(WorkOrderSourceEnum.PERSON.getCode())).collect(Collectors.toList()).size());
        //异常结束告警工单
        model.setAbnormalAlarmWorkOrder(abnormalWorkOrders.stream().filter(workOrder -> workOrder.getSource().equals(WorkOrderSourceEnum.ALARM.getCode())).collect(Collectors.toList()).size());

        //筛选出workOrders中所有的正常结束工单并根据工单类型进行分类
        List<WorkOrder> regularWorkOrders = workOrders.stream().filter(workOrder -> workOrder.getHandleResult() == WorkOrderHandleResultEnum.TYPE_1.getCode()).collect(Collectors.toList());
        //正常结束报事报修工单
        model.setRegularPersonWorkOrder(regularWorkOrders.stream().filter(workOrder -> workOrder.getSource().equals(WorkOrderSourceEnum.PERSON.getCode())).collect(Collectors.toList()).size());
        //正常结束告警工单
        model.setRegularAlarmWorkOrder(regularWorkOrders.stream().filter(workOrder -> workOrder.getSource().equals(WorkOrderSourceEnum.ALARM.getCode())).collect(Collectors.toList()).size());

        //查询抄表计划的工单设备
        //抄表计划工单单独计算
        List<WorkOrder> meterPlanWorks = workOrders.stream().filter(workOrder -> workOrder.getSource().equals(WorkOrderSourceEnum.METERPLAN.getCode()) && workOrder.getStatus().equals(WorkOrderStatusEnum.COMPLETED.getCode())).collect(Collectors.toList());
        //正常结束抄表数量
        Integer regularMeterPlanWorkOrder = 0;
        //异常结束抄表数量
        Integer abnormalMeterPlanWorkOrder = 0;
        if(meterPlanWorks.size() > 0){
            List<Long> meterPlanWorksIds = meterPlanWorks.stream().map(WorkOrder::getId).collect(Collectors.toList());
            List<WorkOrderDevice> workOrderDevices = workOrderDeviceRepository.selectList(new LambdaQueryWrapper<WorkOrderDevice>().in(WorkOrderDevice::getWorkOrderId, meterPlanWorksIds).eq(WorkOrderDevice::getTenantId, tenantId));
            //设备根据工单id分组
            Map<Long, List<WorkOrderDevice>> workOrderDeviceMap = workOrderDevices.stream().collect(Collectors.groupingBy(WorkOrderDevice::getWorkOrderId));
            for (Long workOrderId : meterPlanWorksIds) {
                List<WorkOrderDevice> workOrderDeviceList = workOrderDeviceMap.get(workOrderId);
                if (workOrderDeviceList.size() == 0){
                    regularMeterPlanWorkOrder++;
                    continue;
                }
                if (workOrderDeviceList.stream().allMatch(workOrderDevice -> workOrderDevice.getReadingErrorStatus() == 1)){
                    regularMeterPlanWorkOrder++;
                }else {
                    abnormalMeterPlanWorkOrder++;
                }
            }
        }

        //异常结束抄表计划工单
        model.setAbnormalMeterPlanWorkOrder(abnormalMeterPlanWorkOrder);
        //正常结束抄表计划工单
        model.setRegularMeterPlanWorkOrder(regularMeterPlanWorkOrder);

        //特别关注工单统计
        //获取所有关注人
        List<AttentionManage> attentionManages = attentionManageService.list(new LambdaQueryWrapper<AttentionManage>().eq(AttentionManage::getTenantId, tenantId));
        if (CollectionUtils.isNotEmpty(attentionManages)){
            //获取关注人的id集合
            List<String> attentionIds = attentionManages.stream().map(AttentionManage::getAttentionUid).collect(Collectors.toList());
            //获取关注人的工单
            List<WorkOrder> attentionWorkOrders = workOrders.stream().filter(workOrder -> attentionIds.contains(workOrder.getCreatorId())).collect(Collectors.toList());
            //获取遗留中关注人的工单
            List<WorkOrder> attentionLeftoverWorkOrders = leftoverWorkOrders.stream().filter(workOrder -> attentionIds.contains(workOrder.getCreatorId())).collect(Collectors.toList());
            //写入新产生的已完成工单数量
            model.setAttentionNewWorkOrder(attentionWorkOrders.size());
            //写入特别关注遗留的工单数量
            model.setAttentionLeftoverWorkOrder(attentionLeftoverWorkOrders.size());
            //写入特别关注工单总数量
            model.setAttentionTotalWorkOrder(attentionWorkOrders.size() + attentionLeftoverWorkOrders.size());
            //写入特别关注已完成工单数量
            model.setAttentionCompleteWorkOrder(attentionWorkOrders.stream().filter(workOrder -> workOrder.getStatus() == WorkOrderStatusEnum.COMPLETED.getCode()).collect(Collectors.toList()).size());
            //写入特别关注未完成工单数量
            model.setAttentionIncompleteWorkOrder(model.getAttentionNewWorkOrder() - model.getAttentionCompleteWorkOrder() + model.getAttentionLeftoverWorkOrder());
        }
        return model;
    }

    /**
     * 获取本年度1-12月份三种来源产生的工单数量统计
     *
     * @return Map<Integer, Map<String, Integer>> 每月每种来源的工单数量统计
     */
    @Override
    public Map<Integer, Map<String, Integer>> currentYearStatistics() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();

        // 获取当前年份
        int currentYear = Year.now().getValue();

        // 初始化每个月的统计结果，确保每个来源都有值
        Map<Integer, Map<String, Integer>> monthlyStatistics = IntStream.rangeClosed(1, 12)
            .boxed()
            .collect(Collectors.toMap(
                month -> month,
                month -> Arrays.stream(WorkOrderSourceEnum.values())
                    .collect(Collectors.toMap(
                        source -> source.getCode(),
                        source -> 0
                    ))
            ));

        // 查询本年度所有工单
        List<WorkOrder> workOrders = workOrderRepository.selectList(
            new LambdaQueryWrapper<WorkOrder>()
                .ge(WorkOrder::getCreateTime, LocalDate.of(currentYear, 1, 1).atStartOfDay())
                .le(WorkOrder::getCreateTime, LocalDate.of(currentYear, 12, 31).atStartOfDay())
                .eq(WorkOrder::getTenantId, tenantId)
        );

        // 按月份和来源分组统计工单数量
        Map<Integer, Map<String, Integer>> actualStatistics = workOrders.stream()
            .collect(Collectors.groupingBy(
                workOrder -> workOrder.getCreateTime().getMonth() + 1, // 按月份分组
                Collectors.groupingBy(
                        WorkOrder::getSource, // 按来源分组
                    Collectors.summingInt(workOrder -> 1) // 统计数量
                )
            ));

        // 将实际统计结果合并到初始化的Map中
        actualStatistics.forEach((month, sourceMap) -> {
            monthlyStatistics.get(month).putAll(sourceMap);
        });

        return monthlyStatistics;
    }

    @Override
    public void statisticsExport(HttpServletResponse response) {
        try {
            Map<Integer, Map<String, Integer>> currentYearStatistics = currentYearStatistics();
            List<WorkOrderStatisticsExportModel> workOrderList = new ArrayList<>();
            for (Map.Entry<Integer, Map<String, Integer>> entry : currentYearStatistics.entrySet()) {
                WorkOrderStatisticsExportModel model = new WorkOrderStatisticsExportModel();
                model.setDate(entry.getKey()+"月");
                model.setAlarm(entry.getValue().get(WorkOrderSourceEnum.ALARM.getCode()));
                model.setMeterPlan(entry.getValue().get(WorkOrderSourceEnum.METERPLAN.getCode()));
                model.setProblemReeport(entry.getValue().get(WorkOrderSourceEnum.PERSON.getCode()));
                workOrderList.add(model);
            }
            TenantDomain tenantInfoModel = tenantInfoService.detail(WebFrameworkUtils.getHeaderTenantId());
            String title = tenantInfoModel.getName() + "工单统计表";
            ExcelExportUtils.exportExcelWithTwoHeaders(response, "工单统计", title, workOrderList, WorkOrderStatisticsExportModel.class);
        } catch (Exception e) {
            throw new BaseException("工单统计表导出失败!");
        }
    }
}
