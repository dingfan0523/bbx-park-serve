
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantRoomTemp;

import java.util.List;

/**
 * 会服人员-会议室临时表服务接口
 */
public interface IMeetingAttendantRoomTempService extends IService<MeetingAttendantRoomTemp> {
    /**
     * 添加会服人员-会议室临时数据
     * @param userId 用户id
     * @param roomIdList 会议室id集合
     * @return true/false
     */
    Boolean add(String userId, List<Long> roomIdList);

    /**
     * 查询所有会服人员-会议室临时数据
     * @return 临时数据集合
     */
    List<MeetingAttendantRoomTemp> findAll();
}
