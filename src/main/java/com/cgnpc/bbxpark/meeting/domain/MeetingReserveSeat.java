
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

/***
 * @Description 会议预约座位数据模型实体
 * @author huangyongtao
 * @date 2024/12/24 14:07
 */
@Data
@TableName("bbx_meeting_reserve_seat")
public class MeetingReserveSeat extends BaseExEntity{
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*会议预约id.
	**/
	private Long reserveId;
	/**
	*座位类型;(1->回型桌；2->培训桌；3->讨论桌).
	**/
	private Integer seatType;
	/**
	*座位名称.
	**/
	private String seatName;
	/**
	*座位人员名称.
	**/
	private String personName;
	/**
	*座位分组.
	**/
	private String seatGroup;

}
