
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.util.Date;

/***
 * @Description 会议临时预约数据模型实体
 * @author huangyongtao
 * @date 2025/1/6 15:43
 */
@Data
@TableName("bbx_meeting_temp_reserve")
public class MeetingTempReserve extends BaseExEntity {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*会议室id.
	**/
	private Long roomId;
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

}
