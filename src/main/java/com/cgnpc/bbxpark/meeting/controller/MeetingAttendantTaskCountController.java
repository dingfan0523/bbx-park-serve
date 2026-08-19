
package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantEvaluateDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantRoomModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskPersonCountModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskTopCountModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantCountPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountDetailPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantTaskCountService;
import com.cgnpc.bbxpark.meeting.service.MeetingAttendantTaskCountExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 智慧会议-PC端-会服考核统计
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/meeting/attendant/task/count")
@Api(tags = "智慧会议-PC端-会服考核统计")
public class MeetingAttendantTaskCountController {

    @Autowired
    private IMeetingAttendantTaskCountService meetingAttendantTaskCountService;

    @Autowired
    private MeetingAttendantTaskCountExportService meetingAttendantTaskCountExportService;

    /**
     * 根据人员id查询会议室列表.
     */
    @ApiOperation(value = "根据人员id查询会议室列表")
    @GetMapping(value = "/findRoomList/{userId}")
    public CudResult<List<MeetingAttendantRoomModel>> findRoomList(@PathVariable("userId")@NotNull(message = "用户id不能为空") String userId) {
        return CudResult.success(meetingAttendantTaskCountService.findRoomList(userId));
    }

    /**
     * 会服统计.
     */
    @ApiOperation(value = "会服统计")
    @PostMapping(value = "/attendantTaskCount")
    public CudResult<MeetingAttendantTaskTopCountModel> attendantTaskCount(@RequestBody MeetingAttendantTaskCountParam param) {
        return CudResult.success(meetingAttendantTaskCountService.attendantTaskCount(param));
    }

    /**
     * 会服人员工作量统计.
     */
    @ApiOperation(value = "会服人员工作量统计")
    
    @PostMapping(value = "/attendantTaskPersonCount", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<MeetingAttendantTaskPersonCountModel>> attendantTaskPersonCount(@RequestBody MeetingAttendantTaskCountParam param) {
        return CudResult.success(meetingAttendantTaskCountService.attendantTaskPersonCount(param));
    }

    /**
     * 会服人员工作量统计.
     */
    @ApiOperation(value = "会服人员评分统计")
    
    @PostMapping(value = "/attendantTaskScoreCount", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<MeetingAttendantTaskPersonCountModel>> attendantTaskScoreCount(@RequestBody MeetingAttendantTaskCountParam param) {
        return CudResult.success(meetingAttendantTaskCountService.attendantTaskScoreCount(param));
    }


    /**
     * 获取会服人员统计列表(分页).
     */
    @ApiOperation(value = "获取会服人员统计列表(分页)")
    @PostMapping(value = "/attendantPage")
    public CudResult<IPage<MeetingAttendantTaskPersonCountModel>> attendantPage(@RequestBody MeetingAttendantCountPageParam param) {
        return CudResult.success(meetingAttendantTaskCountService.attendantPage(param));
    }

    /**
     * 查询会议评分详情列表(分页)
     */
    @ApiOperation(value = "查询会议评分详情列表(分页)")
    @PostMapping(value = "/scoreDetailPage")
    public CudResult<IPage<MeetingAttendantEvaluateDetailModel>> scoreDetailPage(@RequestBody MeetingAttendantTaskCountDetailPageParam param) {
        return CudResult.success(meetingAttendantTaskCountService.scoreDetailPage(param));
    }

    /***
     *会服考核统计表导出
     */
    @ApiOperation(value = "会服考核统计表导出")
    @GetMapping(value = "/attendantTaskCountExport")
    public void attendantTaskCountExport(HttpServletResponse response, @ModelAttribute MeetingAttendantCountPageParam param) {
        meetingAttendantTaskCountExportService.attendantTaskCountEasyExport(response, param);
    }

    /***
     *会服评价详情表导出
     */
    @ApiOperation(value = "会服评价详情表导出")
    @GetMapping(value = "/attendantScoreDetailExport")
    public void attendantScoreDetailExport(HttpServletResponse response, @ModelAttribute MeetingAttendantTaskCountDetailPageParam param) {
        meetingAttendantTaskCountExportService.attendantScoreDetailEasyExport(response, param);
    }

}
