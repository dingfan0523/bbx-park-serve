
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

/***
 * @Description 会服任务详情数据模型实体
 * @author huangyongtao
 * @date 2024/12/23 15:19
 */
@Data
@TableName("bbx_meeting_attendant_task_detail")
public class MeetingAttendantTaskDetail extends BaseExEntity {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*会服任务id.
	**/
	private Long taskId;
	/**
	*提交人id.
	**/
	private String submitUid;
	/**
	*提交人名称.
	**/
	private String submitUname;
	/**
	*提交人工号.
	**/
	private String submitStaffid;
	/**
	*会服id.
	**/
	private Long serviceId;
	/**
	*会服名称.
	**/
	private String serviceName;
	/**
	*会服标准.
	**/
	private String serviceStandard;
	/**
	*会服提醒.
	**/
	private String serviceWarn;
	/**
	*会服说明.
	**/
	private String serviceInstructions;
	/**
	*会服属性;(1->普通服务；2->默认服务).
	**/
	private Integer serviceAttribute;
	/**
	*属性类型;(1->会议普通服务，2->视频会议调试服务，3->会议录音服务；4->会议排座服务，5->会议打印服务).
	**/
	private Integer attributeType;

}
