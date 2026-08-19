
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserveSign;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSignModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSignTypeStatisticsModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignParam;

import java.util.List;

/***
 * @Description 会议预约签到服务接口
 * @author huangyongtao
 * @date 2024/8/23 15:43
 */
public interface IMeetingReserveSignService extends IService<MeetingReserveSign> {

    /**
     * PC端/移动端-会议签到分页列表
     */
    IPage<MeetingSignModel> page(MeetingSignPageParam param);

    /**
     * PC端-会议签到列表
     */
    List<MeetingSignModel> findByReserveId(Long reserveId);

    /**
     * PC端-会议签到类型统计
     */
    MeetingSignTypeStatisticsModel typeStatistics(Long reserveId);


    /***
     * PC端-会议代补签
     */
    Boolean signByRepair(MeetingSignParam param);

    /***
     * 移动端-会议签到/补签
     */
    Boolean sign(MeetingSignParam param);

    /***
     * 移动端-会议代签
     */
    Boolean signByBehalf(MeetingSignParam param);

    /***
     * @Description 移动端 - 参会人批量新增
     * @author huangyongtao
     * @date 2024/12/24 14:55
     * @param reserve
     * @param userIdList
     * @param loginId
     */
    void signSaveByReserve(MeetingReserve reserve, List<String> userIdList, String loginId);

    /***
     * @Description 通过会议id删除
     * @author huangyongtao
     * @date 2024/12/25 16:18
     * @param reserveId
     */
    boolean removeByReserveId(Long reserveId);

}
