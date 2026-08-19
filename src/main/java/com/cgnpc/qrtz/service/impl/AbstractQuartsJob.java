package com.cgnpc.qrtz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.cgnpc.cud.utils.UUIDUtil;
import com.cgnpc.qrtz.model.QrtzTaskHistory;
import com.cgnpc.qrtz.service.IQrtzTaskHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Objects;

/******************************
 * 用途说明:
 * 作者姓名: P636016 XIAOJINHUI
 * 创建时间: 2023/9/20 17:42:34
 ******************************/
@Slf4j
@DisallowConcurrentExecution
public abstract class AbstractQuartsJob implements Job {

    public static final String RUN_MSG = "runMsg";
    @Resource
    private IQrtzTaskHistoryService qrtzTaskHistoryService;

    public AbstractQuartsJob() {
    }

    public abstract void executeJob(JobExecutionContext context) throws Exception;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date fireTime = context.getFireTime();
        // 开始时间毫秒
        long startTimeMillis = fireTime.getTime();
        // 任务名称
        String jobName = context.getJobDetail().getKey().getName();
        //组名
        String groupName= context.getJobDetail().getKey().getGroup();

        //实例ID
        String instanceId = context.getFireInstanceId();
        // 触发器名称
        String trigName = "directExec";
        Trigger trigher = context.getTrigger();
        if (BeanUtil.isNotEmpty(trigher)) {
            trigName = trigher.getKey().getName();
        }
        try {
            // 执行业务逻辑
            executeJob(context);
            // 获取执行日志
            String runMsg = (String) context.getJobDetail().getJobDataMap().get(RUN_MSG);
            if(Objects.isNull(runMsg)){
                runMsg="";
            }
            // 结束时间毫秒
            long endTimeMillis = System.currentTimeMillis();
            // 结束时间
            Date endTime = new Date();
            // 计算执行时长
            long duration = endTimeMillis - startTimeMillis;
            JobDetail jobDetail = context.getJobDetail();
            log.debug("jobDetail：{}",JSON.toJSONString(jobDetail));
            // 记录日志
            addLog(instanceId,groupName,jobName, trigName, fireTime, endTime, duration, "任务执行成功!\r\n" + runMsg, IQrtzTaskHistoryService.STATUS_SUCCESS);
        } catch (Exception ex) {
            // 结束时间毫秒
            long endTimeMillis = System.currentTimeMillis();
            // 结束时间
            Date endTime = new Date();
            // 计算执行时长
            long duration = endTimeMillis - startTimeMillis;
            // 记录日志
            addLog(instanceId,groupName,jobName, trigName, fireTime, endTime, duration, "任务执行失败!\r\n" + getExceptionInfo(ex), IQrtzTaskHistoryService.STATUS_FAIL);
            log.error("执行任务出错:" ,ex);
        }
    }

    /**
      * @title: 添加日志
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/21 11:00
      * @description:
      * @param: instanceId 实例ID
      * @param: groupName 组名
      * @param: jobName 任务名称
      * @param: trigName 触发器名称
      * @param: fireTime 触发时间
      * @param: endTime 完成时间
      * @param: duration 执行耗时
      * @param: content 消息内容
      * @param: status 执行状态　1:成功；0:失败
      * @return
      */
    private void addLog(String instanceId,String groupName,String jobName, String trigName, Date fireTime, Date endTime, long duration, String content, String status) {
        QrtzTaskHistory jobLog = new QrtzTaskHistory();
        jobLog.setFireId(UUIDUtil.uuid());
        jobLog.setInstanceId(instanceId);
        jobLog.setTaskGroup(groupName);
        jobLog.setTaskName(jobName);
        jobLog.setSchedName(trigName);
        jobLog.setCompleteTime(endTime.getTime());
        jobLog.setExecState(status);
        jobLog.setFiredTime(fireTime.getTime());
        jobLog.setExpendTime(duration);
        jobLog.setLog(content);
        log.debug("jobLog:{}",JSON.toJSONString(jobLog));
        qrtzTaskHistoryService.save(jobLog);

    }

    /**
      * @title: 获取try-catch中的异常内容
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/21 10:59
      * @description:
      * @param e Exception
      * @return
      */
    public static String getExceptionInfo(Throwable e) {
        String ret = "";
        if (BeanUtil.isEmpty(e)) {
            return ret;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        try {
            log.error(pout.toString());
            ret = new String(out.toByteArray());
        } catch (Exception ex) {
            log.error("获取异常信息错误",ex);
        }finally {
            pout.close();
            try {
                out.close();
            } catch (IOException ex) {
                log.error(ex.getMessage(),e);
            }
        }
        return ret;
    }
}
