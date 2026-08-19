
package com.cgnpc.bbxpark.meeting.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSceneDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSceneModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSceneListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSceneParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingSceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/**
 * 会议室场景控制类
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/meeting/scene")
@Api(tags = "智慧会议-PC端-会议室场景")
public class MeetingSceneController {

    /**
     * 会议室场景服务接口.
     */
    @Autowired
    private IMeetingSceneService meetingRoomSceneService;


    /**
     * PC端-获取会议室场景列表.
     */
    @ApiOperation(value = "PC端-会议室场景列表")
    @PostMapping(value = "/list")
    public CudResult<List<MeetingSceneModel>>list(@RequestBody @Validated({Default.class}) MeetingSceneListParam param) {
        return CudResult.success(meetingRoomSceneService.list(param));
    }


    /**
     * PC端-会议场景详情
     */
    @ApiOperation(value = "PC端-会议场景详情")
    @GetMapping(value = "/detail/{id}")
    public CudResult<MeetingSceneDetailModel> detail(@PathVariable(value = "id")Long id) {
        return CudResult.success(meetingRoomSceneService.detail(id));
    }

    /**
     * PC端-新增会议场景
     */
    @ApiOperation(value = "PC端-新增会议场景")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated({Default.class, InsertGroup.class}) @RequestBody MeetingSceneParam param) {
        return CudResult.success(meetingRoomSceneService.add(param));
    }

    /**
     * PC端-编辑会议场景
     */
    @ApiOperation(value = "PC端-编辑会议室")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) MeetingSceneParam param) {
        return CudResult.success(meetingRoomSceneService.edit(param));
    }

    /**
     * PC端-删除会议室场景
     */
    @ApiOperation(value = "PC端-删除会议室场景")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable("id") @NotNull(message = "场景id不能为空") Long id) {
        return CudResult.success(meetingRoomSceneService.remove(id));
    }
}
