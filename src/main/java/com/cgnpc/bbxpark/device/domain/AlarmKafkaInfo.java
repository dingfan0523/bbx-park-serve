package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("bbx_alarm_kafka_info")
public class AlarmKafkaInfo extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3244184678805352302L;

	/**
	*告警来源(1规则管理、2场景编排).
	**/
	private Integer alarmSource;
	/**
	*规则ID/场景节点ID.
	**/
	private String sourceId;
	/**
	*动作(1产生2恢复).
	**/
	private Integer action;
	/**
	*告警设备DN.
	**/
	private String deviceDn;
	/**
	*告警名称.
	**/
	private String alarmName;
	/**
	*告警等级.
	**/
	private String alarmLevel;
	/**
	*告警时间.
	**/
	private Date alarmTime;
	/**
	*告警描述.
	**/
	private String alarmDesc;

	/**
	 *原始报文
	 **/
	private String contentJson;
	private Long offset;
}
