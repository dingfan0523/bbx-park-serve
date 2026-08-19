
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

/***
 * @Description 会议预约文件数据模型实体
 * @author huangyongtao
 * @date 2024/12/23 15:16
 */
@Data
@TableName("bbx_meeting_reserve_file")
public class MeetingReserveFile extends BaseExEntity{
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*会议预约id.
	**/
	private Long reserveId;
	/**
	*文件名称.
	**/
	private String name;
	/**
	*文件地址.
	**/
	private String url;
	/**
	*是否需要打印;(1->是;0->否).
	**/
	private Integer printing;
	/**
	*份数.
	**/
	private Integer copies;
	/**
	*来源;（1->人工上传； 2->会议室音频文件）.
	**/
	private Integer source;

}
