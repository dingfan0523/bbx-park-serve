package com.cgnpc.bbxpark.ioc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.MeetingPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.MeetingRoomParam;

import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:11
 */
public interface IScreenMeetingService {
    SpecialMeetingOverview getSpecialMeetingOverview();

    List<MeetingRoomUse> getMeetingRoomUsageRanking(String sortOrder);

    MeetingBehaviorInsight getMeetingBehaviorInsight();

    DepartmentActivity getDepartmentActivity();

    PilotMeetingOverview getPilotMeetingOverview();

    List<ServiceStaffWorkload> getServiceWorkloadMatrix(String month);

    List<DailyRoomHealth> getMeetingRoomHealth();

    List<DailySignAnalysis> getEmployeeAttendance();

    EnergySavingOverview getEnergySavingOverview();

    List<DailyEnergyTrend> getDailyEnergyTrend();

    List<ExecutionTrend> getExecutionTrend();

    List<RoomEnergyRank> getRoomEnergyRanking(String sortOrder);

    MeetingGuaranteeOverview getMeetingGuaranteeOverview();

    List<AbnormalCheckItem> getAbnormalCheckItems();

    MeetingServiceOverview getMeetingServiceOverview();

    List<MeetingEvaluation> getMeetingEvaluationList();

    MeetingEvaluationDetail getMeetingEvaluationDetail(String evaluationId);

    IPage<MeetingModel> list(MeetingPageParam param);

    List<MeetingRoomModel> roomList(MeetingRoomParam param);

    List<SimpleMeetingRoomModel> simpleRoomList();

    List<SpaceViewModel> getSpaceView(String sslcCode);
}
