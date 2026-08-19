package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备节能统计
 */
@Data
@TableName("bbx_device_ec_statistics")
public class DeviceEcStatistics implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 节能时长(单位：小时)
     */
    private Double ecDuration;
    /**
     * 节约能耗(单位：kwh)
     */
    private Double ecKwh;
    /**
     * 记录时间
     */
    private Date statisticsTime;
    private Long tenantId;
}
