
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议预约草稿业务数据模型
 * @author huangyongtao
 * @date 2024/12/26 14:46
 */
@Data
public class AppMeetingReserveDraftModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议主题.")
    private String reserveName;

    @ApiModelProperty(value = "草稿(1->是;0->否)")
    private Integer draft;

    @ApiModelProperty(value = "失败原因")
    private String failReason;

}
