
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 会服人员任务分页参数模型
 * @author huangyongtao
 * @date 2024/12/23 15:46
 */
@Data
public class MeetingAttendantTaskPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议id.")
    private Long reserveId;

    @ApiModelProperty(value = "会议名称.")
    private String reserveName;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会服类型;(1->会前布置；2->会中呼叫；3->会后清洁).")
    private Integer serviceType;

    @ApiModelProperty(value = "会服状态;(1->未处理；2->已确认； 3->已完成).")
    private Integer serviceStatus;

    @ApiModelProperty(value = "会服是否有效;(0->有效；1->无效).")
    private Integer serviceValid;

    @ApiModelProperty(value = "会服备注.")
    private String serviceRemark;

    @ApiModelProperty(value = "处理人id.")
    private String handleUid;

    @ApiModelProperty(value = "处理人名称.")
    private String handleUname;

    @ApiModelProperty(value = "处理人工号.")
    private String handleStaffid;

    @ApiModelProperty(value = "处理时间.")
    private Date handleTime;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "排序方式（desc:倒序；asc:正序）")
    private String orderByDirection = "desc";

    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;

}
