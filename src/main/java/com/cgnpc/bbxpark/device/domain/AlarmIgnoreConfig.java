package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警忽略配置数据模型实体
 */
@Data
@TableName("bbx_alarm_ignore_config")
public class AlarmIgnoreConfig extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4459501687939639243L;

	private Long alarmInfoId;

	private String ignoreType;
	/**
	*告警唯一标识.
	**/
	private String alarmUnique;
	/**
	*忽略告警起始时间.
	**/
	private Date ignoreStartTime;
	/**
	*忽略告警终止时间.
	**/
	private Date ignoreEndTime;
	/**
	*启用状态(0启用、停用).
	**/
	private Integer enableStatus;
	/**
	*设置人.
	**/
	private String operator;
	/**
	*设置时间.
	**/
	private Date operateTime;

}
