
package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantRoomTemp;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantRoomTempRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantRoomTempService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 会服人员-会议室临时表服务实现
 * @author 54766
 */
@Service
public class MeetingAttendantRoomTempServiceImpl extends ServiceImpl<MeetingAttendantRoomTempRepository, MeetingAttendantRoomTemp> implements IMeetingAttendantRoomTempService {

    @Override
    public Boolean add(String userId, List<Long> roomIdList) {
        //先删除临时表该用户的数据
        remove(Wrappers.<MeetingAttendantRoomTemp>lambdaQuery().eq(MeetingAttendantRoomTemp::getUserId, userId));
        //将修改后的数据重新存入临时表
        List<MeetingAttendantRoomTemp> list = roomIdList.stream().map(roomId -> {
            MeetingAttendantRoomTemp temp = new MeetingAttendantRoomTemp();
            temp.setUserId(userId);
            temp.setRoomId(roomId);
            return temp;
        }).collect(Collectors.toList());
        return saveBatch(list);
    }

    @Override
    public List<MeetingAttendantRoomTemp> findAll() {
        return list();
    }
}
