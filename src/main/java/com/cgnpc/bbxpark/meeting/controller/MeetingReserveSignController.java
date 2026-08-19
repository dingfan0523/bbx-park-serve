package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSignModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSignTypeStatisticsModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveService;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveSignService;
import com.cgnpc.bbxpark.meeting.service.MeetingReserveExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 会议签到服务控制类
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 11:01
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/meeting/reserve/sign")
@Api(tags = "智慧会议-PC端-会议签到")
public class MeetingReserveSignController {
    @Resource
    private IMeetingReserveSignService meetingReserveSignService;
    @Resource
    private IMeetingReserveService meetingReserveService;
    @Resource
    private MeetingReserveExportService meetingReserveExportService;

    /**
     * PC端-会议签到分页查询
     */
    @ApiOperation(value = "PC端-会议签到分页查询")
    @PostMapping(value = "/page")
    public CudResult<IPage<MeetingSignModel>> page(@RequestBody MeetingSignPageParam param) {
        return CudResult.success(meetingReserveSignService.page(param));
    }

    /**
     * PC端-会议签到类型统计
     */
    @ApiOperation(value = "PC端-会议签到类型统计")
    @GetMapping(value = "/typeStatistics/{reserveId}")
    public CudResult<MeetingSignTypeStatisticsModel> typeStatistics(@PathVariable("reserveId") Long reserveId) {
        return CudResult.success(meetingReserveSignService.typeStatistics(reserveId));
    }

    /**
     * PC端-会议代补签
     */
    @ApiOperation(value = "PC端-会议代补签")
    @PostMapping(value = "/signByRepair")
    public CudResult<Boolean> signByRepair(@RequestBody MeetingSignParam param) {
        return CudResult.success(meetingReserveSignService.signByRepair(param));
    }

    /***
     * PC端-会议签到列表导出
     */
    @ApiOperation(value = "PC端-会议签到列表导出")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, @ModelAttribute MeetingSignPageParam param) {
        meetingReserveExportService.meetingReserveSignExport(response, param);
    }
}
