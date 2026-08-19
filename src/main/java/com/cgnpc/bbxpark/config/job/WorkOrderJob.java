package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * @Description 工单定时任务
 * @author huangyongtao
 * @date 2025/3/31 14:28
 */
@Slf4j
@Component
public class WorkOrderJob {

    @Autowired
    private IWorkOrderService workOrderService;

  /***
   * @Description 工单计划任务的超时提醒（每十五分钟执行一次）
   * @author huangyongtao
   * @date 2025/3/31 14:45
   * @param
   */
    @XxlJob("WorkOrderOutTimeJob")
    public void executePlan(){
         log.info("工单计划超时定时任务开始执行");
        workOrderService.executeWorkOrderOutTime();
    }
}
