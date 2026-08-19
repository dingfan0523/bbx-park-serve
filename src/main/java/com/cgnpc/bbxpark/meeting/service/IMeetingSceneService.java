package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomScene;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSceneDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSceneModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSceneListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSceneParam;

import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/1/6
 * @desc 会议室场景业务层
 */
public interface IMeetingSceneService extends IService<MeetingRoomScene> {
    /**
     * PC端-会议室场景列表
     * @param param 参数
     * @return 场景列表
     */
    List<MeetingSceneModel> list(MeetingSceneListParam param);

    /**
     * PC端-会议场景详情
     * @param id 场景id
     * @return 场景详情
     */
    MeetingSceneDetailModel detail(Long id);

    /**
     * PC端-会议场景保存
     * @param param 参数
     * @return 保存结果
     */
    Boolean add(MeetingSceneParam param);

    /**
     * PC端-会议场景编辑
     * @param param 参数
     * @return 编辑结果
     */
    Boolean edit(MeetingSceneParam param);

    /**
     * PC端-删除会议室场景
     * @param id 场景id
     * @return 删除结果
     */
    Boolean remove(Long id);

    /**
     * 移动端-执行会议场景
     * @param id 场景id
     * @return 执行结果
     */
    Boolean exec(Long id);
}
