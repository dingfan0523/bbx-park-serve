
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 会议室入参数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:29
 */
@Data
public class MeetingRoomParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @Length(max = 100)
    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会议室容量.")
    private Integer roomVolume;

    @ApiModelProperty(value = "空间位置id.")
    private Long spaceId;

    @Length(max = 255)
    @ApiModelProperty(value = "第三方会议室id.")
    private String thirdRoomId;

    @Length(max = 255)
    @ApiModelProperty(value = "会议室备注.")
    private String roomRemark;

    @ApiModelProperty(value = "会议室图片.")
    private String imageUrl;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "设备id集合")
    private List<String> deviceIdList;

}
