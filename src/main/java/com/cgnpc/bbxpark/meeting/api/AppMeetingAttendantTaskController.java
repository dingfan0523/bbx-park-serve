
package com.cgnpc.bbxpark.meeting.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskPageModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingServiceModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingReserveAppSaveParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantTaskService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description 智慧会议-移动端-会服人员任务
 * @author huangyongtao
 * @date 2024/12/23 17:46
 */
@RestController
@RequestMapping("/api/meeting/attendant/task/app")
@Api(tags = "智慧会议-移动端-会服人员任务")
public class AppMeetingAttendantTaskController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(AppMeetingAttendantTaskController.class);

    /**
     * 会服人员任务服务接口.
     */
    @Autowired
    private IMeetingAttendantTaskService meetingAttendantTaskService;

    /**
     * 移动端 - 获取会服人员任务信息.
     */
    @ApiOperation(value = "获取会服人员任务信息")
    @PostMapping(value = "/detail")
    @RequiredToken
    public CudResult<MeetingAttendantTaskModel> detail(@RequestBody MeetingAttendantTaskParam param) {
            return CudResult.success(meetingAttendantTaskService.detail(param));
    }

    /**
     * 移动端 -获取会服人员任务列表(分页).
     */
    @ApiOperation(value = "获取会服人员任务列表(分页)")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<MeetingAttendantTaskPageModel>> pageApp(@RequestBody MeetingAttendantTaskPageParam param) {
            return CudResult.success(meetingAttendantTaskService.pageApp(param));
    }

    /**
     * 移动端 -获取会服人员任务列表.
     */
    @ApiOperation(value = "获取会服人员任务列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<MeetingAttendantTaskModel>> list(@RequestBody MeetingAttendantTaskListParam param) {
            return CudResult.success(meetingAttendantTaskService.list(param));
    }

    /**
     * 移动端 - 获取会前服务详情
     */
    @ApiOperation(value = "获取会前服务详情")
    @PostMapping(value = "/serviceDetail")
    @RequiredToken
    public CudResult<List<MeetingServiceModel>> serviceDetail(@RequestBody MeetingAttendantTaskParam param) {
        AssertUtils.notNull(param.getRoomId(), "会议室id不能为空");
        return CudResult.success(meetingAttendantTaskService.serviceDetail(param));
    }

    /**
     * 移动端-校验会服是否可用
     */
    @ApiOperation(value = "移动端-校验会服是否可用")
    @PostMapping(value = "/service/check")
    @RequiredToken
    public CudResult<Boolean> checkService(@RequestBody MeetingReserveAppSaveParam param) {
        AssertUtils.notNull(param.getRoomId(), "会议室id不能为空");
        return CudResult.success(meetingAttendantTaskService.checkService(param));
    }

    /**
     * 移动端-编辑会前布置的录音服务
     */
    @ApiOperation(value = "移动端-编辑会前布置的录音服务")
    @PostMapping(value = "/serviceBefore/recording/edit")
    @RequiredToken
    public CudResult<Boolean> editServiceBeforeRecording(@RequestBody MeetingReserveAppSaveParam param) {
        return CudResult.success(meetingAttendantTaskService.editServiceBeforeRecording(param));
    }

    /**
     * 移动端-编辑会前布置的普通服务
     */
    @ApiOperation(value = "移动端-编辑会前布置的普通服务")
    @PostMapping(value = "/serviceBefore/ordinary/edit")
    @RequiredToken
    public CudResult<Boolean> editServiceBeforeOrdinary(@RequestBody MeetingReserveAppSaveParam param) {
        return CudResult.success(meetingAttendantTaskService.editServiceBeforeOrdinary(param));
    }

    /**
     * 移动端-编辑会前布置的排座服务
     */
    @ApiOperation(value = "移动端-编辑会前布置的排座服务")
    @PostMapping(value = "/serviceBefore/seat/edit")
    @RequiredToken
    public CudResult<Boolean> editServiceBeforeSeat(@RequestBody MeetingReserveAppSaveParam param) {
        return CudResult.success(meetingAttendantTaskService.editServiceBeforeSeat(param));
    }

    /**
     * 移动端-编辑会前布置的打印服务
     */
    @ApiOperation(value = "移动端-编辑会前布置的打印服务")
    @PostMapping(value = "/serviceBefore/print/edit")
    @RequiredToken
    public CudResult<Boolean> editServiceBeforePrint(@RequestBody MeetingReserveAppSaveParam param) {
        return CudResult.success(meetingAttendantTaskService.editServiceBeforePrint(param));
    }
    /**
     * 移动端-编辑会前布置的会议名称
     */
    @ApiOperation(value = "移动端-编辑会前布置的会议名称")
    @PostMapping(value = "/serviceBefore/reserveName/edit")
    @RequiredToken
    public CudResult<Boolean> editServiceBeforeReserveName(@RequestBody MeetingReserveAppSaveParam param) {
        return CudResult.success(meetingAttendantTaskService.editServiceBeforeReserveName(param));
    }

    /**
     * 移动端-会服确认
     */
    @ApiOperation(value = "会服确认")
    @PostMapping(value = "/confirm")
    @RequiredToken
    public CudResult<Boolean> confirm(@RequestBody MeetingAttendantTaskParam param) {
        return CudResult.success(meetingAttendantTaskService.confirm(param));
    }

    /**
     * 移动端-会服完成
     */
    @ApiOperation(value = "会服完成")
    @PostMapping(value = "/complete")
    @RequiredToken
    public CudResult<Boolean> complete(@RequestBody MeetingAttendantTaskParam param) {
        return CudResult.success(meetingAttendantTaskService.complete(param));
    }


}
