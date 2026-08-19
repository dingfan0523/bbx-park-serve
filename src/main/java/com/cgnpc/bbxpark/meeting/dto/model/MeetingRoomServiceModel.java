
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议室-会服数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:13
 */
@Data
public class MeetingRoomServiceModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会服id")
    private Long serviceId;
    @ApiModelProperty(value = "会服名称")
    private String name;
    @ApiModelProperty(value = "标准")
    private String standard;
    @ApiModelProperty(value = "提醒")
    private String warn;
    @ApiModelProperty(value = "说明")
    private String instructions;
    @ApiModelProperty(value = "提供次数")
    private Integer count;
    @ApiModelProperty(value = "是否可用(1->可用;0->不可用)")
    private Integer validFlag;
}
