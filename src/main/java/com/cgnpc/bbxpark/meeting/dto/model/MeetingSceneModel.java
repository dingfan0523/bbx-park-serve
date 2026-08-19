package com.cgnpc.bbxpark.meeting.dto.model;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议室场景业务数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:25
 */
@Data
public class MeetingSceneModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /** 场景id */
    private Long id;
    /** 场景名称 */
    private String sceneName;
    /** 描述 */
    private String sceneDesc;
}
