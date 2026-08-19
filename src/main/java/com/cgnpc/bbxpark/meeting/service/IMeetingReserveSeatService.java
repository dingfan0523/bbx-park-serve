
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserveSeat;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveSeatModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingReserveSeatParam;

import java.util.List;

/***
 * @Description 会议预约座位服务接口
 * @author huangyongtao
 * @date 2024/12/24 14:13
 */
public interface IMeetingReserveSeatService extends IService<MeetingReserveSeat> {

    /***
     * @Description 批量新增会议排座
     * @author huangyongtao
     * @date 2024/12/24 15:23
     * @param params
     */
    Boolean addBatch(Long reserveId, List<MeetingReserveSeatParam> params);

   /***
    * @Description 根据会议id查询排座信息
    * @author huangyongtao
    * @date 2024/12/25 15:40
    * @param reserveId
    */
    List<MeetingReserveSeatModel> findByReserveId(Long reserveId);
    /**
     * 根据会议id删除
     * @param reserveId
     * @return 结果
     */
    Boolean removeByReserveId(Long reserveId);

}
