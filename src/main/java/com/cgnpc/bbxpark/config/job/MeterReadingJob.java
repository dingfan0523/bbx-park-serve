package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.energy.service.IBranchEnergyFlowService;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordCountService;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordCountService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * @Description 抄表记录定时任务
 * @author huangyongtao
 * @date 2025/3/27 11:19
 */
@Slf4j
@Component
public class MeterReadingJob {

    @Autowired
    private IMeterAutoRecordService meterAutoRecordService;

    @Autowired
    private IMeterPersonRecordCountService meterPersonRecordCountService;

    @Autowired
    private IMeterAutoRecordCountService meterAutoRecordCountService;

    @Autowired
    private IBranchEnergyFlowService branchEnergyFlowService;

  /***
   * @Description 自动抄表任务（每小时执行一次）
   * @author huangyongtao
   * @date 2025/4/21 11:22
   */
    @XxlJob("MeterAutoReadingExecuteJob")
    public void executeMeterAutoReading(){
         log.info("自动抄表任务开始执行");
        meterAutoRecordService.executeAutoReading();
    }

    /***
     * @Description 抄表集抄任务（每天凌晨1点执行）
     * @author huangyongtao
     * @date 2025/4/21 11:22
     */
    @XxlJob("MeterReadingCountExecuteJob")
    public void executeMeterReadingCount(){
        log.info("抄表集抄任务开始执行");
        meterPersonRecordCountService.executePersonReadingCount();
        meterAutoRecordCountService.executeAutoReadingCount();
    }

    /***
     * @Description 生成支路异常提醒（每天凌晨2点执行）
     * @author huangyongtao
     * @date 2026/5/20 11:22
     */
    @XxlJob("AutoReadingRemindExecuteJob")
    public void executeAutoReadingRemind(){
        log.info("生成支路异常提醒任务开始执行");
        branchEnergyFlowService.executeAutoReadingRemind(DeviceReadingTypeEnum.ELECTRICITY.getCode());
        branchEnergyFlowService.executeAutoReadingRemind(DeviceReadingTypeEnum.WATER.getCode());
    }
}
