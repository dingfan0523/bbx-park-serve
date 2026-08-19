package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.property.service.*;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/***
 * @Description 抄表计划定时任务
 * @author huangyongtao
 * @date 2025/3/27 11:19
 */
@Slf4j
@Component
public class MeterReadingPlanJob {

    @Autowired
    private IMeterReadingPlanService meterReadingPlanService;

    @Autowired
    private IMaintainPlanService maintainPlanService;

    @Autowired
    private IInspectionPlanService inspectionPlanService;

    @Autowired
    private IPatrolPlanService patrolPlanService;

    @Autowired
    private IPropertyScheduleService propertyScheduleService;

    @Autowired
    private IInventoryPlanService inventoryPlanService;

    @Autowired
    private ITaskPlanService taskPlanService;


  /***
   * @Description 执行抄表计划（每天凌晨执行）
   * @author huangyongtao
   * @date 2025/3/27 11:22
   * @param
   */
    @XxlJob("MeterReadingExecutePlanJob")
    public void executePlan(){
        Map<Long, String> scheduleMap = propertyScheduleService.getScheduleNameMap(null);
        log.info("抄表计划定时任务开始执行");
        meterReadingPlanService.executePlan(scheduleMap);
        log.info("维保计划定时任务开始执行");
        maintainPlanService.executePlan(scheduleMap);
        log.info("巡检计划定时任务开始执行");
        inspectionPlanService.executePlan(scheduleMap);
        log.info("巡护计划定时任务开始执行");
        patrolPlanService.executePlan(scheduleMap);
        log.info("盘点计划定时任务开始执行");
        inventoryPlanService.executePlan(scheduleMap);
        log.info("任务定时任务开始执行");
        taskPlanService.executePlan(scheduleMap);
    }
}
