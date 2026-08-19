package com.cgnpc.bbxpark.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.param.DoorOpenParam;
import com.cgnpc.bbxpark.device.dto.param.SceneControlParam;
import com.cgnpc.bbxpark.device.service.ISceneControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @create zhaoshuo
 * @time 2024/12/30
 * @desc 场景控制服务接口
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/sceneControl")
@Api(tags = "场景控制")
public class SceneControlController {

    @Autowired
    private ISceneControlService sceneControlService;


    /**
     * 发送控制场景请求.
     */
    @ApiOperation(value = "发送控制场景请求")
    @PostMapping(value = "/sendSceneControl")
    ////@AuthAll
    public CudResult<Boolean> sendSceneControl(@RequestBody SceneControlParam sceneControlParam) {
        return CudResult.success(sceneControlService.sendSceneControl(sceneControlParam));
    }

    /**
     * 发送远程开会.
     */
    @ApiOperation(value = "发送远程开会")
    @GetMapping(value = "/sendRemoteMeeting")
    ////@AuthAll
    public CudResult<Boolean> sendRemoteMeeting() {
        return CudResult.success(sceneControlService.sendRemoteMeeting());
    }

    /**
     * 结束视频会议.
     */
    @ApiOperation(value = "结束视频会议")
    @GetMapping(value = "/sendConcludeMeeting")
    ////@AuthAll
    public CudResult<Boolean> sendConcludeMeeting() {
        return CudResult.success(sceneControlService.sendConcludeMeeting());
    }

    /**
     * 本地开会.
     */
    @ApiOperation(value = "本地开会")
    @GetMapping(value = "/localMeeting")
    ////@AuthAll
    public CudResult<Boolean> localMeeting() {
        return CudResult.success(sceneControlService.localMeeting());
    }

    /**
     * 结束本地开会.
     */
    @ApiOperation(value = "结束本地开会")
    @GetMapping(value = "/localMeetingEnd")
    //@AuthAll
    public CudResult<Boolean> localMeetingEnd() {
        return CudResult.success(sceneControlService.localMeetingEnd());
    }


    /**
     * 设备休眠与告警.
     */
    @ApiOperation(value = "设备休眠与告警")
    @GetMapping(value = "/deviceSleepAndAlarm")
    ////@AuthAll
    public CudResult<Boolean> deviceSleepAndAlarm() {
        return CudResult.success(sceneControlService.deviceSleepAndAlarm());
    }


    /**
     * 黑名单人员扫脸告警并禁用人脸.
     */
    @ApiOperation(value = "黑名单人员扫脸告警并禁用人脸")
    @GetMapping(value = "/blacklistAndAlarm")
    //@AuthAll
    public CudResult<Boolean> blacklistAndAlarm() {
        return CudResult.success(sceneControlService.blacklistAndAlarm());
    }

    /**
     * 中控屏视频输入输出切换
     */
    @ApiOperation(value = "中控屏视频输入输出切换")
    @GetMapping(value = "/switchover")
    //@AuthAll
    public CudResult<Boolean> switchover(@RequestParam("input") Integer input ,@RequestParam("output") Integer output) {
        return CudResult.success(sceneControlService.switchover(input,output));
    }

    /**
     * 设置指定id人员信息
     */
    @ApiOperation(value = "设置指定id人员信息")
    @GetMapping(value = "/personsItemPut")
    //@AuthAll
    public CudResult<Boolean> personsItemPut(@RequestParam("recognition_type") String recognition_type ,@RequestParam("output") Integer id) {
        return CudResult.success(sceneControlService.personsItemPut(recognition_type,id));
    }


    /**
     * 查询当天通行记录
     */
    @ApiOperation(value = "查询当天通行记录")
    @PostMapping(value = "/passesQuery")
    //@AuthAll
    public CudResult<IPage<JSONObject>> passesQuery(CudPageDto param) {
        return CudResult.success(sceneControlService.passesQuery(param));
    }

    /**
     * 远程开门
     */
    @ApiOperation(value = "远程开门")
    @PostMapping(value = "/openDoor")
    public CudResult<Boolean> remoteOpenDoor(@RequestBody DoorOpenParam param) {
        return CudResult.success(sceneControlService.openDoor(param));
    }
}
