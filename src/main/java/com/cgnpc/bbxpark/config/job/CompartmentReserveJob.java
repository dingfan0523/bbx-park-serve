package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.restaurant.service.ICompartmentReserveService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangyongtao
 * @Description 包间预约的定时任务
 * @date 2024/8/2 14:01
 */
@Slf4j
@Component
public class CompartmentReserveJob {

    @Autowired
    private ICompartmentReserveService compartmentReserveService;

    /***
     * @Description 更新过期包间预约 (每分钟执行一次)
     * @author huangyongtao
     * @date 2024/8/2 14:04
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @XxlJob("CompartmentReserveJob")
    public void expiredCompartmentReserve(){
        // log.info("更新过期包间预约的定时任务开始执行");
        compartmentReserveService.updateStatus();
    }
}
