
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会议室分页参数模型
 * @author huangyongtao
 * @date 2024/8/23 15:29
 */
@Data
public class MeetingRoomPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "空间id")
    private Long spaceId;

    @ApiModelProperty(value = "空间位置id集合")
    private List<Long> spaceIdList;

    @ApiModelProperty(value = "会议室备注.")
    private String roomRemark;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;

    @ApiModelProperty(value = "会议室id集合")
    private List<Long> roomIdList;

}
