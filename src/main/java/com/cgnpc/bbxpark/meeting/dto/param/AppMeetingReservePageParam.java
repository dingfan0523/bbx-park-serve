package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 移动端-会议分页参数模型
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 11:59
 */
@Data
public class AppMeetingReservePageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;
    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "是否取消:0->是;1->否")
    private Integer cancelFlag;
    @ApiModelProperty(value = "会议室id集合")
    private List<Long> roomIdList;
    @ApiModelProperty(value = "会议名称")
    private String reserveName;
}
