package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.DepartmentActivity;
import com.cgnpc.bbxpark.ioc.dto.model.MeetingRoomUse;
import com.cgnpc.bbxpark.ioc.dto.model.SpecialMeetingOverview;
import com.cgnpc.bbxpark.meeting.domain.ThirdMeetingRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ThirdMeetingRecordRepository extends BaseMapper<ThirdMeetingRecord> {
    /**
     * 获取专项会议室概览
     * @param tenantId 租户id
     * @return 概览数据
     */
    SpecialMeetingOverview getSpecialMeetingOverview(@Param("tenantId")Long tenantId);

    /**
     * 获取会议室使用排行TOP5
     * @param tenantId 租户id
     * @param sortOrder 排序
     * @return 数据
     */
    List<MeetingRoomUse> getMeetingRoomUsageRanking(@Param("tenantId")Long tenantId, @Param("sortOrder")String sortOrder);

    /**
     * 获取各会议时长范围的会议数量
     * @param tenantId 租户id
     * @return 数据
     */
    Map<String,Long> getMeetingBehaviorInsight(@Param("tenantId")Long tenantId);

    /**
     * 获取部门活跃度
     * @param tenantId 租户id
     * @return 数据
     */
    List<DepartmentActivity.DepartmentMeeting> getDepartmentActivity(@Param("tenantId")Long tenantId);
}
