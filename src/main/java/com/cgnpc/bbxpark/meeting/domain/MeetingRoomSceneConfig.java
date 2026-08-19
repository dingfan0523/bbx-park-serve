package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/1/6
 * @desc 会议室场景配置表
 */
@Data
@TableName("bbx_scene_config")
public class MeetingRoomSceneConfig  extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /** 场景id */
    private Long sceneId ;
    /** 设备id */
    private Long deviceId ;
    /** 设备名称 */
    private String deviceName ;
    /** 物模型标识 */
    private String identifier ;
    /** 物模型名称 */
    private String name ;
    /** 物模型参数json */
    private String args ;
}
