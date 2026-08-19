package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomSceneConfig;

import java.util.List;

/**
 * 会议场景配置业务层
 */
public interface IMeetingSceneConfigService extends IService<MeetingRoomSceneConfig> {
    /**
     * 根据场景id查询配置集合
     * @param sceneId 场景id
     * @return 配置集合
     */
    List<MeetingRoomSceneConfig> listBySceneId(Long sceneId);
    /**
     * 根据场景id删除配置
     * @param sceneId 场景id
     * @return 删除结果
     */
    Boolean removeBySceneId(Long sceneId);
}
