package com.cgnpc.qrtz;

import com.cgnpc.qrtz.controller.JobController;
import com.cgnpc.qrtz.service.impl.QuartzManager;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 在配置文件中是否开启quartz定时任务: true/false
 */

// @Component
public class QuartzApplicationConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Value("${spring.quartz.autoStartup}")
    private boolean autoStartup;

    @Autowired
    private QuartzManager qtzManager;

    @SneakyThrows
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        String jobClassName = "com.cgnpc.qrtz.service.impl.IpmAssNotifyJobService";
        String jobGroupName = "Notify";
        String cronExpression = "0 */10 * * * ?";
        if (autoStartup){
            qtzManager.addOrUpdateJob(JobController.getClass(jobClassName), jobClassName, jobGroupName, cronExpression);
        } else {
            qtzManager.deleteJob(jobClassName, jobGroupName);
        }
    }

}
