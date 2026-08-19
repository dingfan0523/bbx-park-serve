
package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.enums.MeetingRoomSourceEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomService;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomSyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 会议室服务控制类
 * @author huangyongtao
 * @date 2024/8/26 10:05
 */
@Validated
@RestController
@RequestMapping(Constant.BASE_PATH + "/meeting/room")
@Api(tags = "智慧会议-PC端-会议室")
public class MeetingRoomController {

    /**
     * 会议室服务接口.
     */
    @Autowired
    private IMeetingRoomService meetingRoomService;

    @Autowired
    IMeetingRoomSyncService meetingRoomSyncService;

    /**
     * PC端-会议室分页列表
     */
    @ApiOperation(value = "PC端-会议室分页列表")
    @PostMapping(value = "/page")
    public CudResult<IPage<MeetingRoomModel>> page(@RequestBody MeetingRoomPageParam param) {
        return CudResult.success(meetingRoomService.page(param));
    }

    /**
     * 获取会议室列表.
     */
    @ApiOperation(value = "PC端-会议室列表")
    @PostMapping(value = "/list")
    public CudResult<List<MeetingRoomModel>> list(@RequestBody MeetingRoomListParam param) {
        return CudResult.success(meetingRoomService.list(param));
    }

    /**
     * PC端-会议室-设备分页列表
     */
    @ApiOperation(value = "PC端-会议室-设备分页列表")
    @PostMapping(value = "/device/page")
    public CudResult<IPage<MeetingRoomDeviceModel>> pageDevice(@RequestBody MeetingRoomDevicePageParam param) {
        return CudResult.success(meetingRoomService.pageDevice(param));
    }

    /**
     * PC端-会议室-设备分页列表
     */
    @ApiOperation(value = "PC端-会议室-设备列表")
    @PostMapping(value = "/device/list")
    public CudResult<List<MeetingRoomDeviceModel>> listDevice(@RequestBody MeetingRoomDeviceListParam param) {
        return CudResult.success(meetingRoomService.listDevice(param));
    }

    /**
     * PC端-会议室-会服分页列表
     */
    @ApiOperation(value = "PC端-会议室-会服分页列表")
    @PostMapping(value = "/service/page")
    public CudResult<IPage<MeetingRoomServiceModel>> pageService(@RequestBody MeetingRoomServicePageParam param) {
        return CudResult.success(meetingRoomService.pageService(param));
    }

    /**
     * PC端-关联设备
     */
    @ApiOperation(value = "PC端-关联设备")
    @PostMapping(value = "/addDevice")
    public CudResult<Boolean> addDevice(@RequestBody MeetingRoomDeviceParam param) {
        return CudResult.success(meetingRoomService.addDevice(param));
    }

    /**
     * PC端-关联空间
     */
    @ApiOperation(value = "PC端-关联空间")
    @PostMapping(value = "/addSpace")
    public CudResult<Boolean> addSpace(@RequestBody MeetingRoomSpaceParam param) {
        return CudResult.success(meetingRoomService.addSpace(param));
    }

    /**
     * PC端-关联会服
     */
    @ApiOperation(value = "PC端-关联会服")
    @PostMapping(value = "/addService")
    public CudResult<Boolean> addService(@RequestBody MeetingRoomServiceParam param) {
        return CudResult.success(meetingRoomService.addService(param));
    }

    /**
     * PC端-编辑容纳人数
     */
    @ApiOperation(value = "PC端-编辑容纳人数")
    @PostMapping(value = "/editVolume")
    public CudResult<Boolean> editVolume(@RequestBody MeetingRoomVolumeParam param) {
        return CudResult.success(meetingRoomService.editVolume(param));
    }

    /**
     * PC端-编辑提醒
     */
    @ApiOperation(value = "PC端-会议室配置")
    @PostMapping(value = "/config")
    public CudResult<Boolean> config(@RequestBody MeetingRoomConfigParam param) {
        return CudResult.success(meetingRoomService.config(param));
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
     * PC端-新增会议室
     */
    @ApiOperation(value = "PC端-新增会议室")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated({Default.class, InsertGroup.class}) @RequestBody MeetingRoomSaveParam param) {
        param.setSource(MeetingRoomSourceEnum.ADD.getCode());
        return CudResult.success(meetingRoomService.add(param));
    }

    /***
     * PC端-同步会议室列表
     */
    @ApiOperation(value = "PC端-同步会议室列表")
    @PostMapping(value = "/sync")
    public CudResult<Boolean> sync() {
        return CudResult.success(meetingRoomSyncService.sync());
    }

    /**
     * 删除会议室.
     */
    @ApiOperation(value = "删除会议室")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable("id") @NotNull(message = "会议室id不能为空") Long id) {
        return CudResult.success(meetingRoomService.remove(id));
    }

    /**
     * 编辑会议室.
     */
    @ApiOperation(value = "编辑会议室")
 
    @PostMapping(value = "/edit")
    public CudResult edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) MeetingRoomParam param) {
        return CudResult.success(meetingRoomService.edit(param));
    }

    /**
     * PC端-会议室使用统计
     */
    @ApiOperation(value = "PC端-会议室使用统计")
    @GetMapping(value = "/used/count")
    public CudResult<MeetingRoomUsedCountModel> usedCount() {
        return CudResult.success(meetingRoomService.usedCount());
    }

    /**
     * pc端-会服看板会议室的会前布置列表
     */
    @ApiOperation(value = "pc端-会服看板会议室的会前布置列表")
    @PostMapping(value = "/attendant/list")
    public CudResult<List<MeetingRoomAttendantModel>> attendantList(@RequestBody MeetingRoomListParam param) {
        return CudResult.success(meetingRoomService.attendantList(param));
    }
}
