package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("bbx_alarm_device")
public class AlarmDevice extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4166111065543723068L;

    /**
     * 报警ID，关联报警信息表的ID
     */
    @TableField("alarm_id")
    private Long alarmId;

    /**
     * 设备名称
     */
    @TableField("device_name")
    private String deviceName;

    /**
     * 设备ID，关联设备信息表的ID
     */
    @TableField("device_id")
    private Long deviceId;

    /**
     * 空间ID，关联空间信息表的ID
     */
    @TableField("space_id")
    private Long spaceId;

    /**
     * 空间名称
     */
    @TableField("space_name")
    private String spaceName;

    /**
     * 数据版本号，用于乐观锁控制
     */
    @TableField("revision")
    private Integer revision;

    /**
     * 删除标志，0表示未删除，1表示已删除
     */
    @TableField("deleted")
    private Integer deleted;
}