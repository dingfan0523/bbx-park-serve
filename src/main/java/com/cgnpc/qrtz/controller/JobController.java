package com.cgnpc.qrtz.controller;

import java.util.HashMap;
import java.util.Map;

import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.qrtz.domain.JobDetails;
import com.cgnpc.qrtz.service.impl.QuartzManager;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/******************************
 * 用途说明: 定时任务接口
 * 作者姓名: P636016 XIAOJINHUI
 * 创建时间: 2023/9/20 11:35:48
 ******************************/
@RestController
@RequestMapping(value = "/cronjob")
public class JobController extends BaseController {

    @Autowired
    private QuartzManager qtzManager;

    //fixme 此处为了解决奇安信静态扫描漏洞
    @Override
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{""});
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends QuartzJobBean> getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Class<? extends QuartzJobBean>) class1;
    }

    /**
      * @title: 添加定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 14:06
      * @description:
      * @param:
      * @return
      */
    @PostMapping(value = "/addjob")
    public void addjob(@Valid @RequestBody JobDetails details) throws Exception {
        String jobClassName = details.getJobClassName();
        String jobGroupName = details.getJobGroupName();
        String cronExpression = details.getCronExpression();
        qtzManager.addOrUpdateJob(getClass(jobClassName), jobClassName, jobGroupName, cronExpression);
    }

    /**
      * @title: 暂停定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 14:06
      * @description:
      * @param:
      * @return
      */
    @PostMapping(value = "/pausejob")
    public void pausejob(@Valid @RequestBody JobDetails details) throws Exception {
        String jobClassName = details.getJobClassName();
        String jobGroupName = details.getJobGroupName();
        qtzManager.pauseJob(jobClassName, jobGroupName);
    }

    /**
      * @title: 恢复定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 14:06
      * @description:
      * @param:
      * @return
      */
    @PostMapping(value = "/resumejob")
    public void resumejob(@Valid @RequestBody JobDetails details) throws Exception {
        String jobClassName = details.getJobClassName();
        String jobGroupName = details.getJobGroupName();
        qtzManager.resumeJob(jobClassName, jobGroupName);
    }

    /**
      * @title: 发布定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 14:53
      * @description:
      * @param:
      * @return
      */
    @PostMapping(value = "/reschedulejob")
    public void rescheduleJob(@Valid @RequestBody JobDetails details) throws Exception {
        String jobClassName = details.getJobClassName();
        String jobGroupName = details.getJobGroupName();
        String cronExpression = details.getCronExpression();
        qtzManager.addOrUpdateJob(getClass(jobClassName), jobClassName, jobGroupName, cronExpression);
    }

    /**
      * @title: 删除定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 14:07
      * @description:
      * @param:
      * @return
      */
    @PostMapping(value = "/deletejob")
    public void deletejob(@Valid @RequestBody JobDetails details) throws Exception {
        String jobClassName = details.getJobClassName();
        String jobGroupName = details.getJobGroupName();
        qtzManager.deleteJob(jobClassName, jobGroupName);
    }

    /**
      * @title: 查询定时任务
      * @author: P636016 XIAOJINHUI
      * @date: 2023/9/20 14:07
      * @description:
      * @param:
      * @return
      */
    @GetMapping(value = "/queryjob")
    public Map<String, Object> queryjob(@RequestParam(value = "pageNum") Integer pageNum,
                                        @RequestParam(value = "pageSize") Integer pageSize) {
        PageInfo<JobDetails> jobAndTrigger = qtzManager.queryAllJobBean(pageNum, pageSize);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("JobAndTrigger", jobAndTrigger);
        map.put("number", jobAndTrigger.getTotal());
        return map;
    }
}
