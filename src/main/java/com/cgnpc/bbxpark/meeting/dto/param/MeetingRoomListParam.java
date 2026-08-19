
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 会议室列表参数模型
 * @author huangyongtao
 * @date 2024/8/23 15:29
 */
@Data
public class MeetingRoomListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会议室容量.")
    private Integer roomVolume;

    @ApiModelProperty(value = "空间位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "第三方会议室id.")
    private String thirdRoomId;

    @ApiModelProperty(value = "会议室备注.")
    private String roomRemark;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "使用中(0->是;1->否)")
    private Integer used;

    @ApiModelProperty(value = "是否清扫(0->是;1->否)")
    private Integer swept;

    @ApiModelProperty(value = "呼叫中(0->是;1->否)")
    private Integer calling;

    @ApiModelProperty(value = "会议室id集合")
    private List<Long> roomIdList;


}
