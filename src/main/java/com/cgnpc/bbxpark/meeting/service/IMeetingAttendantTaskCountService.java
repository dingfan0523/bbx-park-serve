
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantTask;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantEvaluateDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantRoomModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskPersonCountModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskTopCountModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantCountPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountDetailPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountParam;

import java.util.List;

/***
 * @Description 会服统计服务接口
 * @author huangyongtao
 * @date 2025/2/10 10:08
 */
public interface IMeetingAttendantTaskCountService extends IService<MeetingAttendantTask> {

    /***
     * @Description 会服统计
     * @author huangyongtao
     * @date 2025/2/11 11:38
     * @param param
     */
    MeetingAttendantTaskTopCountModel attendantTaskCount(MeetingAttendantTaskCountParam param);

    /***
     * @Description 会服人员工作量统计
     * @author huangyongtao
     * @date 2025/2/11 16:45
     * @param param
     */
    List<MeetingAttendantTaskPersonCountModel> attendantTaskPersonCount(MeetingAttendantTaskCountParam param);

    /***
     * @Description 会服人员评分统计
     * @author huangyongtao
     * @date 2025/2/12 10:45
     * @param param
     */
    List<MeetingAttendantTaskPersonCountModel> attendantTaskScoreCount(MeetingAttendantTaskCountParam param);

    /***
     * @Description 获取会服人员统计列表(分页).
     * @author huangyongtao
     * @date 2025/2/12 14:43
     * @param param
     */
    IPage<MeetingAttendantTaskPersonCountModel> attendantPage(MeetingAttendantCountPageParam param);

    /***
     * @Description 根据人员id查询会议室列表
     * @author huangyongtao
     * @date 2025/2/12 17:20
     * @param
     */
    List<MeetingAttendantRoomModel> findRoomList(String userId);

    /***
     * @Description 查询会议评分详情列表(分页).
     * @author huangyongtao
     * @date 2025/2/12 14:43
     * @param param
     */
    IPage<MeetingAttendantEvaluateDetailModel> scoreDetailPage(MeetingAttendantTaskCountDetailPageParam param);

}
