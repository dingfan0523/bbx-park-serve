
package com.cgnpc.bbxpark.meeting.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.AppMeetingRoomDeviceModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingRoomDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingRoomModel;
import com.cgnpc.bbxpark.meeting.dto.model.SimpleMeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingRoomDeviceListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingRoomPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingRoomParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingTempReserveParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantService;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomService;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomSyncService;
import com.cgnpc.bbxpark.meeting.service.IMeetingTempReserveService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description 会议室服务控制类
 * @author huangyongtao
 * @date 2024/8/26 10:05
 */
@Validated
@RestController
@RequestMapping("/api/meeting/room")
@Api(tags = "智慧会议-移动端-会议室")
public class AppMeetingRoomController {

    /**
     * 会议室服务接口.
     */
    @Autowired
    private IMeetingRoomService meetingRoomService;

    @Autowired
    private IMeetingRoomSyncService meetingRoomSyncService;

    @Autowired
    IMeetingAttendantService meetingAttendantService;

    @Autowired
    IMeetingTempReserveService meetingTempReserveService;


    /**
     * 移动端-获取会议室列表(分页)
     */
    @ApiOperation(value = "移动端-获取会议室列表(分页)")
    @PostMapping(value = "/app/page")
    @RequiredToken
    public CudResult<IPage<MeetingRoomModel>> appPage(@RequestBody MeetingRoomPageParam param) {
        return CudResult.success(meetingRoomService.appPage(param));
    }

    /**
     * 移动端-分页查询会议室占用情况
     */
    @ApiOperation(value = "移动端-分页查询会议室占用情况")
    @PostMapping(value = "/app/occupy/page")
    @RequiredToken
    public CudResult<IPage<MeetingRoomModel>> appOccupyPage(@RequestBody MeetingRoomPageParam param) {
        return CudResult.success(meetingRoomService.occupyPage(param));
    }
    /**
     * 移动端-会服分页查询会议室占用情况
     */
    @ApiOperation(value = "移动端-会服分页查询会议室占用情况")
    @PostMapping(value = "/app/attendant/occupy/page")
    @RequiredToken
    public CudResult<IPage<MeetingRoomModel>> attendantAppOccupyPage(@RequestBody MeetingRoomPageParam param) {
        return CudResult.success(meetingRoomService.attendantAppOccupyPage(param));
    }

    /**
     * PC端-会议室详情
     */
    @ApiOperation(value = "PC端-会议室详情")
    @GetMapping(value = "/detail/{id}")
    public CudResult<MeetingRoomDetailModel> detail(@PathVariable(value = "id")Long id) {
        return CudResult.success(meetingRoomService.detail(id));
    }

    /**
     * 移动端-会议室简要详情
     */
    @ApiOperation(value = "移动端-会议室简要详情")
    @PostMapping(value = "/app/detail")
    @RequiredToken
    public CudResult<MeetingRoomDetailModel> detail(@RequestBody MeetingRoomParam param ) {
        return CudResult.success(meetingRoomService.simpleDetail(param.getId()));
    }

    /**
     * 移动端-获取会议室会服人员列表
     */
    @ApiOperation(value = "移动端-获取会议室会服人员列表")
    @GetMapping(value = "/app/attendant/detail/{id}")
    @RequiredToken
    public CudResult<List<SimpleMeetingAttendantModel>> attendantDetail(@PathVariable(value = "id") Long id) {
        return CudResult.success(meetingAttendantService.listByRoomId(id));
    }

    /**
     * 编辑会议室图片
     */
    @ApiOperation(value = "编辑会议室图片")
    @PostMapping(value = "/app/edit/image")
    @RequiredToken
    public CudResult<Boolean> editImage(@RequestBody MeetingRoomParam param) {
        return CudResult.success(meetingRoomService.editImage(param));
    }

    /**
     * 移动端-新增会议临时预约.
     */
    @ApiOperation(value = "移动端-新增会议临时预约")
    @PostMapping(value = "/app/temp/reserve/add")
    @RequiredToken
    public CudResult<Boolean> add(@RequestBody MeetingTempReserveParam param) {
        return CudResult.success(meetingTempReserveService.add(param));
    }

    /**
     * 移动端-会议室-设备分页列表
     */
    @ApiOperation(value = "移动端-会议室-设备列表")
    @PostMapping(value = "/app/device/list")
    @RequiredToken
    public CudResult<List<AppMeetingRoomDeviceModel>> listDevice(@RequestBody MeetingRoomDeviceListParam param) {
        return CudResult.success(meetingRoomService.listDeviceApp(param));
    }
}
