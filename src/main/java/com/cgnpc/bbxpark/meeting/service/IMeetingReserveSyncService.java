
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;

/***
 * @Description 会议预约同步服务接口
 * @author huangyongtao
 * @date 2024/10/11 15:44
 */
public interface IMeetingReserveSyncService extends IService<MeetingReserve> {

    /***
     * @Description 同步会议信息
     * @author huangyongtao
     * @date 2024/10/11 9:58
     * @param
     */
    Boolean sync();
}
