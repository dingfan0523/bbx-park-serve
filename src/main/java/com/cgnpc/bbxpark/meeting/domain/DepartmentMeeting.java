package com.cgnpc.bbxpark.meeting.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/2/8
 * @desc 部门会议统计实体
 */
@Data
public class DepartmentMeeting implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     **/
    @ApiModelProperty(value = "开始时间")
    private String departmentId;

    /**
     * 会议类型(ordinary->普通会议;video->视频会议)
     **/
    private String meetingType;

    /**
     * 是否无效:0->是;1->否
     */
    private Integer inValidFlag;

    /**
     * 总数量
     */
    private Long total;
}
