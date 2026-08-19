
package com.cgnpc.bbxpark.meeting.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSceneModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSceneListParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingSceneService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 会议室场景控制类
 */
@RestController
@RequestMapping("/api/meeting/scene/app")
@Api(tags = "智慧会议-移动端-会议室场景")
public class AppMeetingSceneController {

    /**
     * 会议室场景服务接口.
     */
    @Autowired
    private IMeetingSceneService meetingRoomSceneService;


    /**
     * 移动端-获取会议室场景列表.
     */
    @ApiOperation(value = "移动端-会议室场景列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<MeetingSceneModel>> list(@RequestBody MeetingSceneListParam param) {
        return CudResult.success(meetingRoomSceneService.list(param));
    }

    /**
     * 移动端-执行会议场景
     */
    @ApiOperation(value = "移动端-执行会议场景")
    @GetMapping(value = "/exec/{id}")
    @RequiredToken
    public CudResult<Boolean> exec(@PathVariable("id") @NotNull(message = "场景id不能为空") Long id) {
        return CudResult.success(meetingRoomSceneService.exec(id));
    }
}
