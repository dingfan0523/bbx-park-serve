package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/3/13
 * @desc ioc告警等级与第三方平台告警等级映射表
 */
@Data
@TableName("bbx_alarm_level_map")
public class AlarmLevelMap extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4166111065543723068L;

    /**
     * ioc业务告警等级
     */
    private String iocAlarmLevel;

    /**
     * 第三方告警等级
     */
    private String thirdAlarmLevel;

    /**
     * IOT平台
     */
    private Integer thirdType;


}
