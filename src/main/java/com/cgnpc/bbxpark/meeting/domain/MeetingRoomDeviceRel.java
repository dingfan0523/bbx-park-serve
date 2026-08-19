
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议室设备关联数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:12
 */
@Data
@TableName("bbx_meeting_room_device_rel")
public class MeetingRoomDeviceRel extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*会议室id.
	**/
	private Long roomId;
	/**
	*设备id.
	**/
	private Long deviceId;

}
