package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警处置记录数据模型实体
 */
@Data
@TableName("bbx_alarm_handle_record")
public class AlarmHandleRecord extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4230493091363170477L;
	/**
	*告警主键id.
	**/
	private Long alarmId;
	/**
	*环节(1生成告警2告警确认3级别调整4设备停用5结束告警).
	**/
	private Integer link;
	/**
	*告警确认结果(1真实告警、2系统误报、3运维导致).
	**/
	private Integer alarmConfirmResult;
	/**
	*告警状态(1待确认、2已确认、3已结束).
	**/
	private Integer alarmStatus;
	/**
	*告警来源(1规则管理、2场景编排).
	**/
	private Integer alarmSource;
	/**
	*操作人.
	**/
	private String operator;
	/**
	 *操作人工号
	 **/
	private String operatorStaffid;
	/**
	*操作时间.
	**/
	private Date operateTime;
	/**
	*操作备注.
	**/
	private String operateDesc;
	private String work;
	/**
	 * 操作描述
	 */
	private String linkDesc;
}
