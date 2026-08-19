
package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskCountModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskPageModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 智慧会议-PC端-会服人员任务
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/meeting/attendant/task")
@Api(tags = "智慧会议-PC端-会服人员任务")
public class MeetingAttendantTaskController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MeetingAttendantTaskController.class);

    /**
     * 会服人员任务服务接口.
     */
    @Autowired
    private IMeetingAttendantTaskService meetingAttendantTaskService;

    /**
     * 获取会服人员任务信息.
     */
    @ApiOperation(value = "获取会服人员任务信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<MeetingAttendantTaskModel> detail(@PathVariable("id")@NotNull(message = "任务id不能为空") Long id) {
        return CudResult.success(meetingAttendantTaskService.simpleDetail(id));
    }

    /**
     * 获取会服人员任务列表(分页).
     */
    @ApiOperation(value = "获取会服人员任务列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MeetingAttendantTaskModel>> page(@RequestBody MeetingAttendantTaskPageParam param) {
        return CudResult.success(meetingAttendantTaskService.page(param));
    }
    /**
     * PC端 -获取会服人员会中呼叫任务列表.
     */
    @ApiOperation(value = "获取会服人员会中呼叫任务列表")
    
    @PostMapping(value = "/calling/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<MeetingAttendantTaskPageModel>> callingList(@RequestBody MeetingAttendantTaskPageParam param) {
        return CudResult.success(meetingAttendantTaskService.callingList(param));
    }

    /**
     * pc端-会服确认
     */
    @ApiOperation(value = "会服确认")
    
    @PostMapping(value = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> confirm(@RequestBody MeetingAttendantTaskParam param) {
        return CudResult.success(meetingAttendantTaskService.confirm(param));
    }

    /**
     * pc端-会服完成
     */
    @ApiOperation(value = "会服完成")
    
    @PostMapping(value = "/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> complete(@RequestBody MeetingAttendantTaskParam param) {
        return CudResult.success(meetingAttendantTaskService.complete(param));
    }

    /**
     * pc端-获取会服人员完成的会服列表（分页）
     */
    @ApiOperation(value = "获取会服人员完成的会服列表（分页）")
    @PostMapping(value = "/history/page")
    public CudResult<IPage<MeetingAttendantTaskPageModel>> pageHistory(@RequestBody MeetingAttendantTaskPageParam param) {
        return CudResult.success(meetingAttendantTaskService.pageHistory(param));
    }

    /**
     * PC端-会服人员完成会服统计
     */
    @ApiOperation(value = "PC端-会服人员完成会服统计")
    @GetMapping(value = "/complete/count")
    public CudResult<MeetingAttendantTaskCountModel> taskCompleteCount() {
        return CudResult.success(meetingAttendantTaskService.taskCompleteCount());
    }

}
