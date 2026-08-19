package com.cgnpc.bbxpark.device.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.device.dto.param.DoorOpenParam;
import com.cgnpc.bbxpark.device.dto.param.SceneControlParam;

/**
 * @create zhaoshuo
 * @time 2024/12/30
 * @desc 场景控制服务接口
 */
public interface ISceneControlService{

    boolean sendSceneControl(SceneControlParam sceneControlParam);

    /**
     * 远程开会
     * @return
     */
    boolean sendRemoteMeeting();

    /**
     * 结束视频会议
     * @return
     */
    boolean sendConcludeMeeting();

    /**
     * 本地开会.
     */
    boolean localMeeting();

    /**
     * 结束本地开会.
     */
    boolean localMeetingEnd();

    /**
     * 设备休眠与告警.
     */
    boolean deviceSleepAndAlarm();

    /**
     * 黑名单人员扫脸告警并禁用人脸.
     */
    boolean blacklistAndAlarm();

    /**
     * 预约会议
     * @param startTime 会议开始时间
     * @param endTime 会议结束时间
     * @param deviceId 设备id
     * @param meetingName 会议名称
     * @return
     */
    boolean scheduleMeeting(String startTime,String endTime,String deviceId,String meetingName);


    /**
     * 延长会议
     * @param deviceId 设备id
     * @param overtime 延长时间（分钟）
     * @return
     */
    boolean extendMeeting(String deviceId,Integer overtime);

    /**
     * 结束会议
     * @param deviceId 设备id
     * @return
     */
    boolean endMeeting(String deviceId);


    /**
     * 中控屏视频输入输出切换
     * @param input 输入
     * @param output 输出
     * @return
     */
    boolean switchover(Integer input, Integer output);

    /**
     * 调用iot接口发送服务请求
     *
     * @param sceneControlParam
     * @return
     */
    String sendServiceControl(SceneControlParam sceneControlParam);


    /**
     * 设置指定id人员信息
     * @param recognition_type
     * @param id
     * @return
     */
    boolean personsItemPut(String recognition_type, Integer id);

    /**
     * 查询当天通行记录
     *
     * @return
     */
    IPage<JSONObject> passesQuery(CudPageDto param);

    boolean openDoor(DoorOpenParam param);
}
