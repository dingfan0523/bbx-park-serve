package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/1/6
 * @desc 会议室场景表
 */
@Data
@TableName("bbx_scene")
public class MeetingRoomScene extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 会议室id */
    private Long roomId;
    /** 场景名称 */
    private String sceneName;
    /** 描述 */
    private String sceneDesc;
}
