
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;


/**
 * 会服人员-会议室数据模型实体
 */
@Data
@TableName("bbx_meeting_attendant_room")
public class MeetingAttendantRoom extends BaseExEntity {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *用户id.
	 **/
	private String userId;
	/**
	 *用户id.
	 **/
	private Long roomId;
	private Integer status;

}
