package com.cgnpc.bbxpark.ioc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.AbnormalInspectionPointPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.AbnormalPatrolPointPageParam;
import java.util.List;

public interface IScreenSecurityService {
    SecurityOverviewModel getSecurityOverview();

    PatrolExecOverviewModel getPatrolExecOverview();

    List<PatrolRouteTypeStatModel> getPatrolRouteTypeStat();

    List<PatrolRouteListItemModel> getPatrolList();

    List<AlarmTypeDistributionModel> getAlarmTypeDistribution();

    List<AlarmLevelDistributionModel> getAlarmLevelDistribution();

    List<InspectionFaultRankModel> getInspectionFaultTop10();

    List<PersonPassTrendModel> getPersonPassTrend7d();

    IPage<AbnormalPatrolPointModel> pageAbnormalPatrolPoint(AbnormalPatrolPointPageParam param);

    IPage<AbnormalInspectionPointModel> pageAbnormalInspectionPoint(AbnormalInspectionPointPageParam param);
}