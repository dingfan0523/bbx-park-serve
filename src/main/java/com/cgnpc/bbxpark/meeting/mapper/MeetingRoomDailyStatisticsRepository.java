package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.MeetingRoomUtilizationAnalysisModel;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomDailyStatistics;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingRoomAvgRateModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MeetingRoomDailyStatisticsRepository extends BaseMapper<MeetingRoomDailyStatistics> {
    /**
     * 获取30天内会议室日均使用率
     */
    List<MeetingRoomUtilizationAnalysisModel> getMeetingRoomUtilizationAnalysis(@Param("tenantId")Long tenantId);

    /**
     * 获取各会议室30天内平均使用率
     */
    List<MeetingRoomAvgRateModel> getMeetingRoomAvgRate(@Param("tenantId")Long tenantId);
}
