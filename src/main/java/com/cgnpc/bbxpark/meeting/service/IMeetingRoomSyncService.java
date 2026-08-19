
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;

/***
 * @Description 会议室同步服务接口
 * @author huangyongtao
 * @date 2024/10/11 15:39
 */
public interface IMeetingRoomSyncService extends IService<MeetingRoom> {

    /***
     * @Description 同步会议室信息
     * @author huangyongtao
     * @date 2024/10/11 9:58
     * @param
     */
    Boolean sync();

}
