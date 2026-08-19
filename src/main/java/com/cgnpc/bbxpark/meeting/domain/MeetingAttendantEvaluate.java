
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

/***
 * @Description 会议服务评价数据模型实体
 * @author huangyongtao
 * @date 2024/12/23 15:23
 */
@Data
@TableName("bbx_meeting_attendant_evaluate")
public class MeetingAttendantEvaluate extends BaseExEntity {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*会议预约id.
	**/
	private Long reserveId;
	/**
	*分数.
	**/
	private Integer score = 0;
	/**
	*内容.
	**/
	private String content;
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

}
