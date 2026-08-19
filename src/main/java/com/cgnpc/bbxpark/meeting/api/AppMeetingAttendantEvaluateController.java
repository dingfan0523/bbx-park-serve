
package com.cgnpc.bbxpark.meeting.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantEvaluateModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluateListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluatePageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluateParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantEvaluateService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 会议服务评价服务控制类
 * @author huangyongtao
 * @date 2024/12/23 17:41
 */
@RestController
@RequestMapping("/api/meeting/Attendant/evaluate/app")
@Api(tags = "智慧会议-移动端-服务评价")
public class AppMeetingAttendantEvaluateController {

    /**
     * 会议服务评价服务接口.
     */
    @Autowired
    private IMeetingAttendantEvaluateService meetingAttendantEvaluateService;



    /**
     * 获取会议服务评价列表(分页).
     */
    @ApiOperation(value = "获取会议服务评价列表(分页)")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<MeetingAttendantEvaluateModel>> page(@RequestBody MeetingAttendantEvaluatePageParam param) {
            return CudResult.success(meetingAttendantEvaluateService.page(param));
    }

    /**
     * 获取会议服务评价列表.
     */
    @ApiOperation(value = "获取会议服务评价列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<MeetingAttendantEvaluateModel>> list(@RequestBody MeetingAttendantEvaluateListParam param) {
            return CudResult.success(meetingAttendantEvaluateService.list(param));
    }

    /**
     * 新增会议服务评价.
     */
    @ApiOperation(value = "新增会议服务评价")
    @PostMapping(value = "/add")
    @RequiredToken
    public CudResult<Boolean> add(@Validated({Default.class, InsertGroup.class}) @RequestBody MeetingAttendantEvaluateParam param) {
            return CudResult.success(meetingAttendantEvaluateService.add(param));
    }



    /**
     * 编辑会议服务评价.
     */
    @ApiOperation(value = "编辑会议服务评价")
    @PostMapping(value = "/edit")
    @RequiredToken
    public CudResult<Boolean> edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) MeetingAttendantEvaluateParam param) {
            return CudResult.success(meetingAttendantEvaluateService.edit(param));
    }

}
