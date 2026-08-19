package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.energy.service.IEnergyAbnormalRemindService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @create zhaoshuo
 * @time 2025/4/22
 * @desc 能源提醒定时任务
 */
@Slf4j
@Component
public class EnergyRemindJob {

    @Autowired
    private IEnergyAbnormalRemindService energyAbnormalRemindService;

    @XxlJob("EnergyAbnormalRemindTask")
    public void EnergyAbnormalRemindTask() {
        //能耗偏差提醒
        log.info("EnergyAbnormalRemindTask start");
        energyAbnormalRemindService.searchEnergyAbnormal();
    }

    @XxlJob("SleepEnergyAbnormalTask")
    public void SleepEnergyAbnormalTask() {
        //休息时段能耗过高提醒
        log.info("SleepEnergyAbnormalTask start");
        energyAbnormalRemindService.searchSleepEnergy();
    }
}
