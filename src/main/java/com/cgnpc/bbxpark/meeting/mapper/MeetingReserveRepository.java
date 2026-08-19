
package com.cgnpc.bbxpark.meeting.mapper;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.enums.MeetingReserveStatusEnum;
import com.cgnpc.bbxpark.common.enums.MeetingReserveTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.ioc.dto.model.WeekRoomHealth;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.dto.model.AppMeetingReserveModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveModel;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/***
 * @Description 会议预约数据操作接口
 * @author huangyongtao
 * @date 2024/8/23 15:39
 */
@Repository
public interface MeetingReserveRepository extends BaseMapper<MeetingReserve> {
    /**
     * 移动端-分页查询会议
     */
    IPage<AppMeetingReserveModel> pageApp(IPage<AppMeetingReserveModel> page, @Param("condition") AppMeetingReservePageParam condition);

    /**
     * 移动端-查询分页数量
     */
    Integer pageCount(@Param("condition") AppMeetingReservePageParam condition);


    /***
     * @Description 分页查询会议预约的信息
     * @author huangyongtao
     * @date 2024/8/26 14:09
     * @param page
     * @param condition
     */
    IPage<MeetingReserveModel> pageReserve(IPage<MeetingReserveModel> page, @Param("condition") MeetingReservePageParam condition);

    /**
     * 分页查询我的会议信息
     */
    IPage<MeetingReserveModel> pageMyReserve(IPage<MeetingReserveModel> page, @Param("condition") MeetingReservePageParam condition);

    /***
     * @Description 查询会议预约信息
     * @author huangyongtao
     * @date 2024/8/26 15:00
     * @param condition
     */
    List<MeetingReserveModel> findReserve(@Param("condition") MeetingReserveListParam condition);

    /***
     * @Description 分页查询签到人的会议预约信息
     * @author huangyongtao
     * @date 2024/8/28 14:09
     * @param page
     * @param condition
     */
    IPage<MeetingReserveModel> pageSignReserve(IPage<MeetingReserveModel> page, @Param("condition") MeetingReservePageParam2 condition);

    /**
     * 查询会议室每日健康度-会议场次
     * @return 数据
     */
    List<WeekRoomHealth> getMeetingRoomHealth(@Param("tenantId")Long tenantId);


    default Boolean checkTime(MeetingReserveParam param){
        AssertUtils.isFalse(MeetingReserveTypeEnum.VIDEO.getValue().equals(param.getMeetingType()) && DateUtil.between(param.getStartTime(), param.getEndTime(), DateUnit.MINUTE) < 30, "视频会议的会议时长至少30分钟！");
        AssertUtils.isFalse(new Date().after(param.getStartTime()), "会议预约开始时间已过期！");
        int count = selectCount(Wrappers.<MeetingReserve>lambdaQuery().eq(MeetingReserve::getRoomId, param.getRoomId())
                .lt(MeetingReserve::getStartTime, param.getEndTime())
                .gt(MeetingReserve::getEndTime, param.getStartTime())
                .eq(MeetingReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .in(MeetingReserve::getStatus, Arrays.asList(MeetingReserveStatusEnum.START.getCode(), MeetingReserveStatusEnum.GOING.getCode()))
                .eq(MeetingReserve::getDraft, (int) Status.disabled.getKey())
                .eq(MeetingReserve::getCancelFlag, (int) Status.disabled.getKey()));
        AssertUtils.isFalse(count > 0, "会议室该预约时间段已被占用，请重新预约");
        return true;
    }
}

