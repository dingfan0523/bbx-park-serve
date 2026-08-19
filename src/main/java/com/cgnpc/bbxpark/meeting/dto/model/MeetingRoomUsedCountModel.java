package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议室使用情况统计模型
 * @author huangyongtao
 * @date 2025/1/8 14:50
 */
@Data
public class MeetingRoomUsedCountModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总数")
    private Long totalNum = 0L;

    @ApiModelProperty(value = "使用中的数量")
    private Long usedNum = 0L;

    @ApiModelProperty(value = "空闲的数量")
    private Long freeNum = 0L;
}
