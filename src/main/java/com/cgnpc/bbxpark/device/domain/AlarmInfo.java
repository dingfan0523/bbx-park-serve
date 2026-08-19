
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("bbx_alarm_info")
public class AlarmInfo  extends BaseExEntity implements Serializable{
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4166111065543723068L;

	/**
	*告警名称.
	**/
	private String alarmName;
	/**
	 *告警级别.
	 **/
	private String alarmLevel;
	/**
	*告警唯一标识(设备DN+规则ID/场景节点ID).
	**/
	private String alarmUnique;
	/**
	*告警设备DN.
	**/
	private String deviceDn;
	/**
	*告警状态(1待确认、2已确认、3已结束).
	**/
	private Integer alarmStatus;
	/**
	*告警确认结果(1真实告警、2系统误报、3运维导致、4级别调整).
	**/
	private Integer alarmConfirmResult;
	/**
	*首次告警时间.
	**/
	private Date alarmFirstTime;
	/**
	*末次告警时间.
	**/
	private Date alarmLastTime;
	/**
	*告警次数.
	**/
	private Integer alarmCount;
	/**
	*告警来源(1规则管理、2场景编排).
	**/
	private Integer alarmSource;
	/**
	*告警描述.
	**/
	private String alarmDesc;
	/**
	*告警结束类型(1故障排除2设备下线3系统误报4忽略告警5手动结束6设备删除7设备禁用).
	**/
	private Integer alarmEndType;
	/**
	*告警恢复描述.
	**/
	private String alarmRecoveryDesc;
	/**
	 * 告警类型
	 */
	private String alarmRuleType;
	/**
	 * 空间位置
	 */
	private String spaceAddr;
	/**
	 * 告警规则id
	 */
	private String alarmRuleId;
	/**
	 * 空间位置id
	 */
	private String spaceAddrId;
	/**
	 * 告警设备
	 */
	private String alarmDevice;

	/**
	 * 告警结束时间
	 */
	private Date alarmEndTime;

	/**
	 * 是否转工单
	 */
	private Short wrokOrder;
}
