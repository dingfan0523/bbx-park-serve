package com.cgnpc.qrtz.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/******************************
 * 用途说明: 定时任务详情
 * 作者姓名: P636016 XIAOJINHUI
 * 创建时间: 2023/9/20 11:22:13
 ******************************/
@Data
@NotNull
public class JobDetails implements Serializable {
    private String cronExpression;
    @NotBlank(message = "任务类名不能为空")
    private String jobClassName;
    private String triggerGroupName;
    private String triggerName;
    @NotBlank(message = "组名不能为空")
    private String jobGroupName;
    private String jobName;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date startTime;
    private String timeZone;
    private String status;
}
