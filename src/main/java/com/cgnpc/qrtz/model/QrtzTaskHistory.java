package com.cgnpc.qrtz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务执行日志
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("qrtz_task_history")
public class QrtzTaskHistory implements Serializable {

private static final long serialVersionUID=1L;

    @TableField("SCHED_NAME")
    private String schedName;

    @TableField("INSTANCE_ID")
    private String instanceId;

    @TableId(value = "FIRE_ID")
    private String fireId;

    @TableField("TASK_NAME")
    private String taskName;

    @TableField("TASK_GROUP")
    private String taskGroup;

    @TableField("FIRED_TIME")
    private Long firedTime;

    @TableField("FIRED_WAY")
    private String firedWay;

    @TableField("COMPLETE_TIME")
    private Long completeTime;

    @TableField("EXPEND_TIME")
    private Long expendTime;

    @TableField("REFIRED")
    private Integer refired;

    @TableField("EXEC_STATE")
    private String execState;

    @TableField("LOG")
    private String log;


}
