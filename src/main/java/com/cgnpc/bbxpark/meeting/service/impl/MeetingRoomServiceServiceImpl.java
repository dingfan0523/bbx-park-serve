package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomService;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomServiceRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomServiceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/10/9 17:07
 */
@Service("meetingRoomServiceService")
public class MeetingRoomServiceServiceImpl extends ServiceImpl<MeetingRoomServiceRepository, MeetingRoomService> implements IMeetingRoomServiceService {
    @Override
    public List<MeetingRoomService> listByRoomIds(List<Long> roomIds) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        return list(Wrappers.<MeetingRoomService>lambdaQuery().in(MeetingRoomService::getRoomId, roomIds).eq(tenantId != null, MeetingRoomService::getTenantId, tenantId));
    }

    @Override
    public boolean removeByRoomId(Long roomId) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        return remove(Wrappers.<MeetingRoomService>lambdaQuery().eq(MeetingRoomService::getRoomId, roomId).eq(tenantId != null, MeetingRoomService::getTenantId, tenantId));
    }
}
