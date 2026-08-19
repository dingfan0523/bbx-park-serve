
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议室数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:13
 */
@Data
@TableName("bbx_meeting_room_service")
public class MeetingRoomService extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * 会议室id
     */
    private Long roomId;
    /**
     * 会服id
     */
    private Long serviceId;
    /**
     * 提供次数
     */
    private Integer provideCount;
    /**
     * 乐观锁
     **/
    private Integer revision;
}
