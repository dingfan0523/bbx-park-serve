package com.cgnpc.qrtz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cgnpc.qrtz.domain.JobDetails;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/******************************
 * 用途说明: 定时任务管理
 * 作者姓名: P636016 XIAOJINHUI
 * 创建时间: 2023/9/20 11:24:59
 ******************************/
@Slf4j
@Component
public class QuartzManager {

    @Autowired
    private Scheduler scheduler;

    /**
      * @title: 创建or更新任务，存在则更新不存在创建
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:17
      * @description:
      * @param jobClass     任务类
      * @param jobName      任务名称
      * @param jobGroupName 任务组名称
      * @param jobCron      cron表达式
      * @return
      */
    public void addOrUpdateJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobCron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger==null) {
                addJob(jobClass, jobName, jobGroupName, jobCron);
            } else {
                if (trigger.getCronExpression().equals(jobCron)) {
                    return;
                }
                updateJob(jobName, jobGroupName, jobCron);
            }
        } catch (SchedulerException e) {
            log.error("【创建or更新任务，存在则更新不存在创建】时出错：{}",e.getMessage(),e);
        }
    }

    /**
      * @title: 添加定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:17
      * @description:
      * @param jobClass     任务实现类
      * @param jobName      任务名称
      * @param jobGroupName 任务组名
      * @param jobCron      cron表达式(如：0/5 * * * * ? )
      * @return
      */
    public void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobCron) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                    .startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobCron)).startNow().build();

            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("【添加定时任务】时出错：{}",e.getMessage(),e);
        }
    }

    /**
      * @title: 添加定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:18
      * @description:
      * @param jobClass     任务实现类
      * @param jobName      任务名称
      * @param jobGroupName 任务组名
      * @param jobTime      执行时间
      * @return
      */
    public void addJob(Class<? extends Job> jobClass, String jobName, String jobGroupName, int jobTime) {
        addJob(jobClass, jobName, jobGroupName, jobTime, -1);
    }

    /**
      * @title: 添加定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:20
      * @description:
      * @param jobClass     任务实现类
      * @param jobName      任务名称
      * @param jobGroupName 任务组名
      * @param jobTime      执行时间
      * @param jobTimes     重试次数
      * @return
      */
    public void addJob(Class<? extends Job> jobClass, String jobName, String jobGroupName, int jobTime, int jobTimes) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)// 任务名称和组构成任务key
                    .build();
            // 使用simpleTrigger规则
            Trigger trigger;
            if (jobTimes < 0) {
                trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
                        .startNow().build();
            } else {
                trigger = TriggerBuilder
                        .newTrigger().withIdentity(jobName, jobGroupName).withSchedule(SimpleScheduleBuilder
                                .repeatSecondlyForever(1).withIntervalInSeconds(jobTime).withRepeatCount(jobTimes))
                        .startNow().build();
            }
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("【添加定时任务】时出错：{}",e.getMessage(),e);
        }
    }

    /**
      * @title: 更新定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:21
      * @description:
      * @param jobName      任务名称
      * @param jobGroupName 任务组名
      * @param jobTime      执行时间
      * @return
      */
    public void updateJob(String jobName, String jobGroupName, String jobTime) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).build();
            // 重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("【更新定时任务】时出错：{}",e.getMessage(),e);
        }
    }

    /**
      * @title: 删除定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:22
      * @description:
      * @param jobName      任务名称
      * @param jobGroupName 任务组名
      * @return
      */
    public void deleteJob(String jobName, String jobGroupName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
            scheduler.deleteJob(new JobKey(jobName, jobGroupName));
        } catch (Exception e) {
            log.error("【删除定时任务】时出错：{}",e.getMessage(),e);
        }
    }

    /**
      * @title: 暂停定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:23
      * @description:
      * @param jobName      任务名称
      * @param jobGroupName 任务组名
      * @return
      */
    public void pauseJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error("【暂停定时任务】时出错：{}",e.getMessage(),e);
        }
    }

    /**
      * @title: 恢复定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:24
      * @description:
      * @param jobName      任务名称
      * @param jobGroupName 任务组名
      * @return
      */
    public void resumeJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error("【恢复定时任务】时出错：{}",e.getMessage(),e);
        }
    }

    /**
     * @title: 立即执行一个任务
     * @author: P636016 XIAOJINHUI
     * @date: 2023/9/20 16:24
     * @description:
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @return
     */
    public void runAJobNow(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error("【立即执行一个任务】时出错：{}",e.getMessage(),e);
        }
    }

    /**
      * @title: 分页获取定时任务详情
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:25
      * @description:
      * @param pageNum
      * @param pageSize
      * @return
      */
    public PageInfo<JobDetails> queryAllJobBean(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobDetails> jobList = null;
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    JobDetails jobDetails = new JobDetails();
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        jobDetails.setCronExpression(cronTrigger.getCronExpression());
                        jobDetails.setTimeZone(cronTrigger.getTimeZone().getDisplayName());
                    }
                    jobDetails.setTriggerGroupName(trigger.getKey().getName());
                    jobDetails.setTriggerName(trigger.getKey().getGroup());
                    jobDetails.setJobGroupName(jobKey.getGroup());
                    jobDetails.setJobName(jobKey.getName());
                    jobDetails.setStartTime(trigger.getStartTime());
                    jobDetails.setJobClassName(scheduler.getJobDetail(jobKey).getJobClass().getName());
                    jobDetails.setNextFireTime(trigger.getNextFireTime());
                    jobDetails.setPreviousFireTime(trigger.getPreviousFireTime());
                    jobDetails.setStatus(scheduler.getTriggerState(trigger.getKey()).name());
                    jobList.add(jobDetails);
                }
            }
        } catch (SchedulerException e) {
            log.error("【分页获取定时任务详情】时出错：{}",e.getMessage(),e);
        }
        return new PageInfo<>(jobList);
    }

    /**
      * @title: 获取所有计划中的任务列表
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:26
      * @description:
      * @param:
      * @return
      */
    public List<Map<String, Object>> queryAllJob() {
        List<Map<String, Object>> jobList = null;
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("jobName", jobKey.getName());
                    map.put("jobGroupName", jobKey.getGroup());
                    map.put("description", "trigger:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    map.put("jobStatus", triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        map.put("jobTime", cronExpression);
                    }
                    jobList.add(map);
                }
            }
        } catch (SchedulerException e) {
            log.error("【获取所有计划中的任务列表】时出错：{}",e.getMessage(),e);
        }
        return jobList;
    }

    /**
      * @title: 获取所有正在运行的任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 16:26
      * @description:
      * @param:
      * @return
      */
    public List<Map<String, Object>> queryRunJon() {
        List<Map<String, Object>> jobList = null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                Map<String, Object> map = new HashMap<>();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                map.put("jobName", jobKey.getName());
                map.put("jobGroupName", jobKey.getGroup());
                map.put("description", "trigger:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put("jobStatus", triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put("jobTime", cronExpression);
                }
                jobList.add(map);
            }
        } catch (SchedulerException e) {
            log.error("【获取所有正在运行的任务】时出错：{}",e.getMessage(),e);
        }
        return jobList;
    }
}
