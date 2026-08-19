package com.cgnpc.bbxpark.meeting.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSignModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveService;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveSignService;
import com.cgnpc.bbxpark.meeting.service.MeetingReserveExportService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 会议签到服务控制类
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 11:01
 */
@RestController
@RequestMapping("/api/meeting/reserve/sign/app")
@Api(tags = "智慧会议-移动端-会议签到")
public class AppMeetingReserveSignController {
    @Resource
    private IMeetingReserveSignService meetingReserveSignService;
    @Resource
    private IMeetingReserveService meetingReserveService;
    @Resource
    private MeetingReserveExportService meetingReserveExportService;

    /**
     * 移动端-会议签到分页查询
     */
    @ApiOperation(value = "移动端-会议签到分页查询")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<MeetingSignModel>> pageApp(@RequestBody MeetingSignPageParam param) {
        return CudResult.success(meetingReserveSignService.page(param));
    }

    /**
     * 移动端-会议签到/补签
     */
    @ApiOperation(value = "移动端-会议签到/补签")
    @PostMapping(value = "/sign")
    @RequiredToken
    public CudResult<Boolean> sign(@RequestBody MeetingSignParam param) {
        return CudResult.success(meetingReserveSignService.sign(param));
    }

    /**
     * 移动端-会议代签到
     */
    @ApiOperation(value = "移动端-会议代签到")
    @PostMapping(value = "/signByBehalf")
    @RequiredToken
    public CudResult<Boolean> signByBehalfApp(@RequestBody MeetingSignParam param) {
        return CudResult.success(meetingReserveSignService.signByBehalf(param));
    }
}
