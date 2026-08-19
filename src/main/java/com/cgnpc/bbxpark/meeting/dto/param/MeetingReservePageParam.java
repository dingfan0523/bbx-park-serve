
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会议预约分页参数模型
 * @author huangyongtao
 * @date 2024/8/23 15:27
 */
@Data
public class MeetingReservePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会议名称.")
    private String reserveName;
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "发起人.")
    private String reserveUname;
    @ApiModelProperty(value = "是否无效:1->是;0->否")
    private Integer inValidFlag;
    @ApiModelProperty(value = "会议阶段(10->待开始;20->进行中;30->已结束)")
    private Integer status;
    // 接收前端参数时的转换格式（核心）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;
    @ApiModelProperty(value = "会议结束时间.")
    // 接收前端参数时的转换格式（核心）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
    @ApiModelProperty(value = "用户id")
    private String userId;
    private String reserveUid;
    @ApiModelProperty(value = "空间id集合")
    private List<Long> spaceIdList;
}
