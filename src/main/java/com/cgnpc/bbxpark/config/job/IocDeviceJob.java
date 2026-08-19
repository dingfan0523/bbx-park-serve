package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.device.service.IDeviceEcStatisticsService;
import com.cgnpc.bbxpark.device.service.IDeviceHealthRecordService;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * @Description 物联设备定时任务
 * @author huangyongtao
 * @date 2025/4/23 14:28
 */
@Slf4j
@Component
public class IocDeviceJob {

    @Autowired
    private IIocDeviceService iocDeviceService;
    @Autowired
    private IDeviceHealthRecordService deviceHealthRecordService;
    @Autowired
    private IDeviceEcStatisticsService deviceEcStatisticsService;

  /***
   * @Description 物联设备更新状态任务（每五分钟执行一次）
   * @author huangyongtao
   * @date 2025/4/23 14:45
   * @param
   */
    @XxlJob("IocDeviceStatusJob")
    public void executeIocDeviceStatus(){
         log.info("物联设备更新状态任务开始执行");
        iocDeviceService.updateIotDeviceStatus();
    }

    /**
     * 物联设备健康数据(在线情况及告警情况)统计(每天凌晨1点统计)
     */
    @XxlJob("DeviceHealthDataJob")
    public void statisticsDeviceHealthData(){
        log.info("物联设备健康数据(在线情况及告警情况)统计开始执行");
        deviceHealthRecordService.statisticsDeviceHealthData();
    }

    /**
     * 物联设备能耗统计(每天凌晨1点30统计)
     */
    @XxlJob("DeviceEcStatisticsJob")
    public void calculateYesterdayStatistics(){
        log.info("物联设备能耗统计任务开始执行");
        deviceEcStatisticsService.calculateYesterdayStatistics();
    }
}
