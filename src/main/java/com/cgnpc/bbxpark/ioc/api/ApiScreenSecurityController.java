package com.cgnpc.bbxpark.ioc.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.ioc.dto.model.AlarmLevelDistributionModel;
import com.cgnpc.bbxpark.ioc.dto.model.AlarmTypeDistributionModel;
import com.cgnpc.bbxpark.ioc.dto.model.AbnormalInspectionPointModel;
import com.cgnpc.bbxpark.ioc.dto.model.AbnormalPatrolPointModel;
import com.cgnpc.bbxpark.ioc.dto.model.InspectionFaultRankModel;
import com.cgnpc.bbxpark.ioc.dto.model.PatrolExecOverviewModel;
import com.cgnpc.bbxpark.ioc.dto.model.PatrolRouteListItemModel;
import com.cgnpc.bbxpark.ioc.dto.model.PatrolRouteTypeStatModel;
import com.cgnpc.bbxpark.ioc.dto.model.PersonPassTrendModel;
import com.cgnpc.bbxpark.ioc.dto.model.SecurityOverviewModel;
import com.cgnpc.bbxpark.ioc.dto.param.AbnormalInspectionPointPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.AbnormalPatrolPointPageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.ioc.service.IScreenSecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api/dtwin/security"})
@Api(tags = {"大屏-智慧安防接口"})
public class ApiScreenSecurityController {
    private static final long MOCK_SEED = 20260524L;

    @Autowired
    private IScreenSecurityService screenSecurityService;

    @GetMapping({"/getSecurityOverview"})
    @ApiOperation("安防概览")
    public CudResult<SecurityOverviewModel> getSecurityOverview() {
        return CudResult.success(screenSecurityService.getSecurityOverview());
    }

    @GetMapping({"/getPatrolExecOverview"})
    @ApiOperation("巡更执行概览")
    public CudResult<PatrolExecOverviewModel> getPatrolExecOverview() {
        return CudResult.success(screenSecurityService.getPatrolExecOverview());
    }

    @GetMapping({"/getPatrolRouteTypeStat"})
    @ApiOperation("巡更路线类型统计")
    public CudResult<List<PatrolRouteTypeStatModel>> getPatrolRouteTypeStat() {
        return CudResult.success(screenSecurityService.getPatrolRouteTypeStat());
    }

    @GetMapping({"/patrol/list"})
    @ApiOperation("巡更列表")
    public CudResult<List<PatrolRouteListItemModel>> getPatrolList() {
        return CudResult.success(screenSecurityService.getPatrolList());
    }

    @GetMapping({"/getAlarmTypeDistribution"})
    @ApiOperation("告警类型分布")
    public CudResult<List<AlarmTypeDistributionModel>> getAlarmTypeDistribution() {
        return CudResult.success(screenSecurityService.getAlarmTypeDistribution());
    }

    @GetMapping({"/getAlarmLevelDistribution"})
    @ApiOperation("告警等级分布")
    public CudResult<List<AlarmLevelDistributionModel>> getAlarmLevelDistribution() {
        return CudResult.success(screenSecurityService.getAlarmLevelDistribution());
    }

    @GetMapping({"/getInspectionFault"})
    @ApiOperation("巡检故障排行TOP10")
    public CudResult<List<InspectionFaultRankModel>> getInspectionFaultTop10() {
        return CudResult.success(screenSecurityService.getInspectionFaultTop10());
    }

    @GetMapping({"/getPersonPassTrend"})
    @ApiOperation("人员通行趋势(7日内)")
    public CudResult<List<PersonPassTrendModel>> getPersonPassTrend7d() {
        return CudResult.success(screenSecurityService.getPersonPassTrend7d());
    }

    @ApiOperation(value = "异常巡更点分页列表")
    @PostMapping(value = "/abnormalPatrolPoint/page")
    public CudResult<IPage<AbnormalPatrolPointModel>> pageAbnormalPatrolPoint(@RequestBody AbnormalPatrolPointPageParam param) {
        return CudResult.success(screenSecurityService.pageAbnormalPatrolPoint(param));
    }

    @ApiOperation(value = "异常巡检点分页列表")
    @PostMapping(value = "/abnormalInspectionPoint/page")
    public CudResult<IPage<AbnormalInspectionPointModel>> pageAbnormalInspectionPoint(@RequestBody AbnormalInspectionPointPageParam param) {
        return CudResult.success(screenSecurityService.pageAbnormalInspectionPoint(param));
    }
}
