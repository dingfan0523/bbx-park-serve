
package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.enums.MeetingAttendantTaskTypeEnum;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.AppMeetingReserveDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveFileModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveModel;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveService;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveSyncService;
import com.cgnpc.bbxpark.meeting.service.MeetingReserveExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;

/***
 * @Description 会议预约服务控制类
 * @author huangyongtao
 * @date 2024/8/26 11:13
 */
@RestController
@Validated
@RequestMapping(Constant.BASE_PATH + "/meeting/reserve")
@Api(tags = "智慧会议-PC端-会议")
@Slf4j
public class MeetingReserveController {


    /**
     * 会议预约服务接口.
     */
    @Autowired
    private IMeetingReserveService meetingReserveService;
    @Autowired
    private IMeetingReserveSyncService meetingReserveSyncService;
    @Autowired
    private MeetingReserveExportService meetingReserveExportService;

    /**
     * PC端-获取会议分页列表
     */
    @ApiOperation(value = "PC端-获取会议分页列表")
    @PostMapping(value = "/page")
    public CudResult<IPage<MeetingReserveModel>> page(@RequestBody MeetingReservePageParam param) {
        return CudResult.success(meetingReserveService.page(param));
    }

    /**
     * PC端-获取会议分页列表
     */
    @ApiOperation(value = "PC端-我的会议分页列表")
    @PostMapping(value = "/my/page")
    public CudResult<IPage<MeetingReserveModel>> myPage(@RequestBody MeetingReservePageParam param) {
        return CudResult.success(meetingReserveService.myPage(param));
    }

    /**
     * PC端-获取会议-附件分页列表
     */
    @ApiOperation(value = "PC端-获取会议附件分页列表")
    @PostMapping(value = "/file/page")
    public CudResult<IPage<MeetingReserveFileModel>> pageFile(@RequestBody MeetingReserveFilePageParam param) {
        return CudResult.success(meetingReserveService.pageFile(param));
    }

    /**
     * 移动端-保存会议附件
     */
    @ApiOperation(value = "PC端-保存会议附件")
    @PostMapping(value = "/file/save")
    public CudResult<Boolean> saveFile(@RequestBody AppMeetingFileParam param) {
        param.setSource(1);
        return CudResult.success(meetingReserveService.saveFile(param));
    }

    /**
     * 移动端-保存会议附件
     */
    @ApiOperation(value = "PC端-删除会议附件")
    @GetMapping(value = "/file/remove/{id}")
    public CudResult<Boolean> removeFile(@PathVariable("id")Long id) {
        return CudResult.success(meetingReserveService.removeFile(id));
    }

    /***
     * PC端-编辑会议实际结束时间
     */
    @ApiOperation(value = "PC端-编辑会议实际结束时间")
    @PostMapping(value = "/updateRealEndTime")
    public CudResult<Boolean> updateRealEndTime(@RequestBody MeetingReserveTimeParam param) {
        return CudResult.success(meetingReserveService.updateRealEndTime(param));
    }

    /***
     * PC端-会议设为有效
     */
    @ApiOperation(value = "PC端-会议设为有效")
    @PostMapping(value = "/valid")
    public CudResult<Boolean> valid(@RequestBody MeetingReserveValidParam param) {
        return CudResult.success(meetingReserveService.valid(param));
    }

    /***
     * PC端-会议设为无效
     */
    @ApiOperation(value = "PC端-会议设为无效")
    @PostMapping(value = "/inValid")
    public CudResult<Boolean> inValid(@RequestBody MeetingReserveInValidParam param) {
        return CudResult.success(meetingReserveService.inValid(param));
    }

    /**
     * PC端-会议详情.
     */
    @ApiOperation(value = "PC端-会议详情")
    @GetMapping(value = "/detail/{id}")
    public CudResult<MeetingReserveDetailModel> detail(@PathVariable("id") Long id) {
        return CudResult.success(meetingReserveService.detail(id));
    }

    /***
     * PC端-更新会议预约任务
     */
    @ApiOperation(value = "PC端-更新会议预约任务")
    @PostMapping(value = "/updateReserveTask")
    public CudResult updateReserveTask() {
        meetingReserveService.realStartTimeTask();
        meetingReserveService.realEndTimeTask();
        return CudResult.success(meetingReserveService.invalidTask());
    }
    /***
     * PC端-会议预约人签到通知任务
     */
    @ApiOperation(value = "PC端-会议预约人签到通知任务")
    @PostMapping(value = "/signNoticeTask")
    public CudResult signNoticeTask() {
        return CudResult.success(meetingReserveService.signNoticeTask());
    }

    /***
     * PC端-会议列表导出
     */
    @ApiOperation(value = "PC端-会议列表导出")
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, @ModelAttribute MeetingReservePageParam param) {
        meetingReserveExportService.meetingReserveExport(response, param);
    }

    /***
     * PC端-同步会议列表
     */
    @ApiOperation(value = "PC端-同步会议列表")
    @PostMapping(value = "/sync")
    public CudResult sync() {
        return CudResult.success(meetingReserveSyncService.sync());
    }

    /**
     * PC端-新增会议预约.
     */
    @ApiOperation(value = "PC端-新增会议预约")
    @PostMapping(value = "/add")
    @Deprecated
    public CudResult add(@Validated({Default.class, InsertGroup.class}) @RequestBody MeetingSaveParam param) {
        AssertUtils.notNull(param.getStartTime(), "开始时间不能为空");
        AssertUtils.notNull(param.getEndTime(), "结束时间不能为空");
        return CudResult.success(meetingReserveService.add(param));
    }

    /**
     * PC端-删除会议预约.
     */
    @ApiOperation(value = "PC端-删除会议预约")
    @PostMapping(value = "/remove")
    @Deprecated
    public CudResult remove(@RequestBody MeetingReserveParam param) {
        return CudResult.success(meetingReserveService.remove(param.getId()));
    }

    /**
     * pc端-会服页面的详情
     */
    @ApiOperation(value = "pc端-会服页面的详情.")
    
    @GetMapping(value = "/serviceDetail/{id}")
    public CudResult<AppMeetingReserveDetailModel> serviceDetail(@PathVariable(value = "id")Long id) {
        return CudResult.success(meetingReserveService.serviceDetail(id, MeetingAttendantTaskTypeEnum.BEFORE.getValue()));
    }
}
