
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议预约数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:08
 */
@Data
@TableName("bbx_meeting_reserve")
public class MeetingReserve extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	 * 第三方id
	 */
	private String thirdReserveId;
	/**
	*会议室id.
	**/
	private Long roomId;
	/**
	*会议主题.
	**/
	private String reserveName;
	/**
	*会议室名称.
	**/
	private String roomName;
	/**
	*会议开始时间.
	**/
	private Date startTime;
	/**
	*会议结束时间.
	**/
	private Date endTime;
	/**
	*预约人id.
	**/
	private String reserveUid;
	/**
	*预约人名称.
	**/
	private String reserveUname;
	/**
	*预约人工号.
	**/
	private String reserveStaffid;

	/**
	 * 预约人部门id
	 */
	private String reserveDepartmentId;
	/**
	 * 预约人部门
	 */
	private String reserveDepartment;
	/**
	*会议实际开始时间.
	**/
	private Date realStartTime;
	/**
	*会议实际结束时间.
	**/
	private Date realEndTime;
	/**
	 * 实际开始类型:1->参会人签到;2->会议到达开始时间
	 */
	private Integer realStartType;
	/**
	 * 实际结束类型:1->发起人结束会议;2->系统自动结束;3->会服结束会议
	 */
	private Integer realEndType;
	/**
	*是否无效:1->是;0->否
	**/
	private Integer inValidFlag;
	/**
	 *操作人id.
	 **/
	private String operateUid;
	/**
	 *操作人名称.
	 **/
	private String operateUname;
	/**
	 *操作人工号.
	 **/
	private String operateStaffid;
	/**
	 *操作时间
	 */
	private Date operateTime;
	/**
	 *操作原因
	 **/
	private String operateReason;
	/**
	 *是否必须签到(1->必须签到;0->自愿签到).
	 **/
	private Integer mustSignFlag;
	/**
	 * 是否允许代签(1->允许;0->不允许)
	 */
	private Integer behalfSignFlag;
	/**
	 *是否允许补签(1->允许;0->不允许).
	 **/
	private Integer replenishSignFlag;
	/**
	 * 规则最后修改时间
	 */
	private Date lastRuleTime;
	/**
	 *是否取消(1->是;0->否)
	 **/
	private Integer cancelFlag;
	/**
	 * 取消时间
	 */
	private Date cancelTime;
	/**
	 * 会议状态/会议阶段(1->待开始;2->进行中;3->已结束)
	 */
	private Integer status;
	/**
	 * 会议类型(ordinary->普通会议;video->视频会议)
	 */
	private String meetingType;
	/**
	 * 草稿(1->是;0->否)
	 */
	private Integer draft;
	/**
	 * 失败原因
	 */
	private String failReason;
	/**
	 * 参会人数
	 */
	private Integer participantNumber;
	/**
	 * 实际参会人数
	 */
	private Integer realParticipantNumber;
	/**
     * 延时(单位:分钟)
	 */
	private Integer delayTime;
	/**
	 * 会议摘要
	 */
	private String summary;
	/**
	 * 会服留言
	 */
	private String serveRemark;
	/**
	 * 保留音频(1->是;0->否)
	 */
	private Integer retainedAudio;
	/**
	 * 参会方式(initiator->发起方(主会场);participator->参与方(分会场))
	 */
	private String way;
	/**
	 * 涉密会议(1->是;0->否)
	 */
	private Integer confidentiality;
	/**
	 * 需要密码(1->是;0->否)
	 */
	private Integer needPassword;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * 是否可用(1->是;0->否)
	 **/
	private Integer validFlag;

	/**
	 * 会议时长(分钟)
	 **/
	private Long duration;

	/**
	 * 部门id
	 **/
	private String departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
	/**
	 * 乐观锁
	 */
	@Version
	@TableField(fill = FieldFill.INSERT)
	private Integer revision;
}
