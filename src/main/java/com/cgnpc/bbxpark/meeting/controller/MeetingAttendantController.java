
package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantRoomParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 智慧会议-PC端-会服人员
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/meeting/attendant")
@Api(tags = "智慧会议-PC端-会服人员")
public class MeetingAttendantController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MeetingAttendantController.class);

    /**
     * 会服人员服务接口.
     */
    @Autowired
    private IMeetingAttendantService meetingAttendantService;

    /**
     * 获取会服人员列表(分页).
     */
    @ApiOperation(value = "获取会服人员列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MeetingAttendantModel>> page(@RequestBody MeetingAttendantPageParam param) {
        return CudResult.success(meetingAttendantService.page(param));
    }

    /**
     * 获取会服人员信息.
     */
    @ApiOperation(value = "获取会服人员信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<MeetingAttendantDetailModel> detail(@PathVariable("id")@NotNull(message = "会服人员id不能为空") String id) {
        return CudResult.success(meetingAttendantService.detail(id));
    }

    /**
     * PC端-关联会议室
     */
    @ApiOperation(value = "PC端-关联会议室")
    @PostMapping(value = "/bindRoom")
    public CudResult<Boolean> bindRoom(@RequestBody MeetingAttendantRoomParam param) {
        return CudResult.success(meetingAttendantService.bindRoom(param));
    }

    @ApiOperation(value = "查询会服人员存在未完成任务的会议室")
    @GetMapping(value = "/findUnCompleteTask/{id}")
    public CudResult existUnCompleteTask(@PathVariable("id")@NotNull(message = "会服人员id不能为空") String id) {
        return CudResult.success(meetingAttendantService.findUnCompleteTask(id));
    }

    /**
     * 获取登录的会服人员信息.
     */
    @ApiOperation(value = "获取登录的会服人员信息")
    @GetMapping(value = "/detail/login")
    public CudResult<MeetingAttendantModel> detailByLogin() {
        return CudResult.success(meetingAttendantService.detailByLogin());
    }
}
