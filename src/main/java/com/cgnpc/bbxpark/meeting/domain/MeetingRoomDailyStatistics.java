package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备节能统计
 */
@Data
@TableName("bbx_meeting_room_daily_statistics")
public class MeetingRoomDailyStatistics implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 会议室id
     */
    private Long roomId;
    /**
     * 会议时长
     */
    private Double duration;
    /**
     * 利用率
     */
    private Double utilizationRate;
    /**
     * 记录时间
     */
    private Date statisticsTime;
    private Long tenantId;
}
