package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.MeetingPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.MeetingRoomParam;
import com.cgnpc.bbxpark.ioc.service.IScreenMeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/dtwin/meeting"})
@Api(tags = {"会议统计接口"}  )
public class ApiScreenMeetingController {
    @Autowired
    private IScreenMeetingService meetingService;

    @GetMapping("/getSpecialOverview")
    @ApiOperation("专项会议室概览")
    public CudResult<SpecialMeetingOverview> getSpecialMeetingOverview() {
        return CudResult.success(meetingService.getSpecialMeetingOverview());
    }

    @GetMapping("/getRoomUseRank")
    @ApiOperation("会议室使用排行列表")
    public CudResult<List<MeetingRoomUse>> getMeetingRoomUsageRanking(
            @ApiParam(value = "排序方式: ASC 正序, DESC 倒序", defaultValue = "DESC")
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder) {
        return CudResult.success(meetingService.getMeetingRoomUsageRanking(sortOrder));
    }

    @GetMapping("/getBehaviorInsight")
    @ApiOperation("会议行为洞察")
    public CudResult<MeetingBehaviorInsight> getMeetingBehaviorInsight() {
        return CudResult.success(meetingService.getMeetingBehaviorInsight());
    }

    @GetMapping("/getDepartmentActivity")
    @ApiOperation("部门活跃度")
    public CudResult<DepartmentActivity> getDepartmentActivity() {
        return CudResult.success(meetingService.getDepartmentActivity());
    }

    @GetMapping("/getPilotOverview")
    @ApiOperation("试点会议室概览")
    public CudResult<PilotMeetingOverview> getPilotMeetingOverview() {
        return CudResult.success(meetingService.getPilotMeetingOverview());
    }

    @GetMapping("/getServiceWorkloadMatrix")
    @ApiOperation("服务质量-工作负荷矩阵")
    public CudResult<List<ServiceStaffWorkload>> getServiceWorkloadMatrix(
            @ApiParam(value = "时间范围(月份)", example = "2024-06")
            @RequestParam(required = false) String month) {
        return CudResult.success(meetingService.getServiceWorkloadMatrix(month));
    }

    @GetMapping("/getRoomHealth")
    @ApiOperation("会议室健康度")
    public CudResult<List<DailyRoomHealth>> getMeetingRoomHealth() {
        return CudResult.success(meetingService.getMeetingRoomHealth());
    }

    @GetMapping("/getSignAnalysis")
    @ApiOperation("员工参会行为分析(30天)")
    public CudResult<List<DailySignAnalysis>> getEmployeeAttendance() {
        return CudResult.success(meetingService.getEmployeeAttendance());
    }


    @GetMapping("/getEnergyOverview")
    @ApiOperation("节能概览")
    public CudResult<EnergySavingOverview> getEnergySavingOverview() {
        return CudResult.success(meetingService.getEnergySavingOverview());
    }

    @GetMapping("/getEnergyDailyTrend")
    @ApiOperation("日均节能趋势（30天）")
    public CudResult<List<DailyEnergyTrend>> getDailyEnergyTrend() {
        return CudResult.success(meetingService.getDailyEnergyTrend());
    }

    @GetMapping("/getEnergyExecutionTrend")
    @ApiOperation("节能方案执行趋势（30天）")
    public CudResult<List<ExecutionTrend>> getExecutionTrend() {
        return CudResult.success(meetingService.getExecutionTrend());
    }

    @GetMapping("/getEnergyRoomRank")
    @ApiOperation("会议室节能排名")
    public CudResult<List<RoomEnergyRank>> getRoomEnergyRanking(
            @ApiParam(value = "排序方式: ASC 正序, DESC 倒序", defaultValue = "DESC")
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder) {
        return CudResult.success(meetingService.getRoomEnergyRanking(sortOrder));
    }

    @GetMapping("/getGuaranteeOverview")
    @ApiOperation("会议保障专项概览")
    public CudResult<MeetingGuaranteeOverview> getMeetingGuaranteeOverview() {
        return CudResult.success(meetingService.getMeetingGuaranteeOverview());
    }

    @GetMapping("/getGuaranteeAbnormalItems")
    @ApiOperation("异常检查项列表")
    public CudResult<List<AbnormalCheckItem>> getAbnormalCheckItems() {
        return CudResult.success(meetingService.getAbnormalCheckItems());
    }

    @GetMapping("/getServiceOverview")
    @ApiOperation("会议服务项目概览")
    public CudResult<MeetingServiceOverview> getMeetingServiceOverview() {
        return CudResult.success(meetingService.getMeetingServiceOverview());
    }

    @GetMapping("/evaluation/list")
    @ApiOperation("会议评价列表")
    public CudResult<List<MeetingEvaluation>> getMeetingEvaluationList() {
        return CudResult.success(meetingService.getMeetingEvaluationList());
    }

    @GetMapping("/evaluation/detail")
    @ApiOperation("会议评价详情")
    public CudResult<MeetingEvaluationDetail> getMeetingEvaluationDetail(
            @ApiParam(value = "评价ID", required = true)
            @RequestParam String id) {
        return CudResult.success(meetingService.getMeetingEvaluationDetail(id));
    }

    @PostMapping("/list")
    @ApiOperation("会议列表")
    public CudResult<IPage<MeetingModel>> list(@RequestBody MeetingPageParam param) {
        return CudResult.success(meetingService.list(param));
    }

    @PostMapping("/room/list")
    @ApiOperation("会议室列表")
    public CudResult<List<MeetingRoomModel>> roomList(@RequestBody MeetingRoomParam param) {
        return CudResult.success(meetingService.roomList(param));
    }

    @GetMapping("/room/simpleList")
    @ApiOperation("简单的会议室列表(下拉框)")
    public CudResult<List<SimpleMeetingRoomModel>> simpleList() {
        return CudResult.success(meetingService.simpleRoomList());
    }

    @GetMapping(value = "/getSpaceView")
    @ApiOperation(value = "会议室空间楼层高亮展示列表")
    public CudResult<List<SpaceViewModel>> getSpaceView(@ApiParam(value = "所属空间物模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(meetingService.getSpaceView(sslcCode));
    }
}
