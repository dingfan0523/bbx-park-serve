package com.cgnpc.bbxpark.meeting.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @create zhaoshuo
 * @time 2025/1/6
 * @desc 会议室场景控制类
 */
@Validated
@RestController
@RequestMapping(Constant.BASE_PATH + "/meeting/room/Scene")
@Api(tags = "智慧会议-PC端-会议室场景")
public class MeetingRoomSceneController {
}
