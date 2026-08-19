package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomSceneConfig;
import com.cgnpc.bbxpark.meeting.mapper.MeetingSceneConfigRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingSceneConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhaoshuo
 * @time 2025/1/6
 * @desc 会议室场景配置业务层
 */
@Service
public class MeetingSceneConfigServiceImpl extends ServiceImpl<MeetingSceneConfigRepository, MeetingRoomSceneConfig> implements IMeetingSceneConfigService {
    @Override
    public List<MeetingRoomSceneConfig> listBySceneId(Long sceneId) {
        return list(Wrappers.<MeetingRoomSceneConfig>lambdaQuery().eq(MeetingRoomSceneConfig::getSceneId, sceneId));
    }

    @Override
    public Boolean removeBySceneId(Long sceneId) {
        return remove(Wrappers.<MeetingRoomSceneConfig>lambdaQuery().eq(MeetingRoomSceneConfig::getSceneId, sceneId));
    }
}
