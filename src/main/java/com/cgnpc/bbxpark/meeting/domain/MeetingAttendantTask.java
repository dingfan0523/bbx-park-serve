
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.util.Date;

/***
 * @Description 会服人员任务数据模型实体
 * @author huangyongtao
 * @date 2024/12/23 15:21
 */
@Data
@TableName("bbx_meeting_attendant_task")
public class MeetingAttendantTask extends BaseExEntity {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*会议id.
	**/
	private Long reserveId;
	/**
	*会议名称.
	**/
	private String reserveName;
	/**
	*会议室id.
	**/
	private Long roomId;
	/**
	*会议室名称.
	**/
	private String roomName;
	/**
	*会服类型;(1->会前布置；2->会中呼叫；3->会后清洁).
	**/
	private Integer serviceType;
	/**
	*会服状态;(1->未处理；2->已确认； 3->已完成).
	**/
	private Integer serviceStatus;
	/**
	*会服是否有效;(1->有效；0->无效).
	**/
	private Integer serviceValid;
	/**
	*会服备注.
	**/
	private String serviceRemark;
	/**
	*处理人id.
	**/
	private String handleUid;
	/**
	*处理人名称.
	**/
	private String handleUname;
	/**
	*处理人工号.
	**/
	private String handleStaffid;
	/**
	*处理时间.
	**/
	private Date handleTime;

	@Version
	@TableField(fill = FieldFill.INSERT)
	private Integer revision;

}
