package com.cgnpc.qrtz.service.impl;

import cn.hutool.core.date.DateUtil;
import com.cgnpc.qrtz.service.IIpmAssAllService;
import com.cgnpc.qrtz.service.impl.AbstractQuartsJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.util.Date;

/******************************
 * 用途说明: 超时节点消息提醒
 * 作者姓名: P636016 XIAOJINHUI
 * 创建时间: 2023/9/20 14:30:03
 ******************************/
@Slf4j
public class IpmAssNotifyJobService extends AbstractQuartsJob {
    @Resource
    private IIpmAssAllService assAllService;

    @Override
    public void executeJob(JobExecutionContext context) throws Exception {
        context.getJobDetail().getJobDataMap().forEach(
                (k,v)->log.info("param,key:{},value:{}",k,v)
        );
        assAllService.notifyTodo();
    }
}
