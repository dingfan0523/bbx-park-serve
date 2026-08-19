
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议室-会服入参数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:29
 */
@Data
public class MeetingRoomServiceListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会服名称")
    private String name;
}
