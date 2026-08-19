package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.settings.service.IAwardService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * @Description 评优评奖定时任务
 * @author huangyongtao
 * @date 2025/11/13 14:28
 */
@Slf4j
@Component
public class AwardJob {

    @Autowired
    private IAwardService awardService;

  /***
   * @Description 评优评奖定时任务（每天凌晨2点执行）
   * @author huangyongtao
   * @date 2025/11/13 14:45
   */
    @XxlJob("AwardDisplayTimeJob")
    public void executeAwardDisplayTime(){
         log.info("评优评奖定时任务开始执行");
        awardService.executeAwardDisplayTime();
    }
}
