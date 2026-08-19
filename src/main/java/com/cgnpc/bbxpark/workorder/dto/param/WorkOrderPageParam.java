
package com.cgnpc.bbxpark.workorder.dto.param;



import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单主分页参数模型
 */
@Data
public class WorkOrderPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "工单名称.")
    private String name;

    @ApiModelProperty(value = "工单编码.")
    private String code;

    @ApiModelProperty(value = "工单类型(报修工单:repair).")
    private String type;

    @ApiModelProperty(value = "工单来源（人工上报：person；告警触发：alarm）.")
    private String source;

    @ApiModelProperty(value = "工单描述.")
    private String desc;

    @ApiModelProperty(value = "问题图片.")
    private String problemPictureUrl;

    @ApiModelProperty(value = "处理人名称.")
    private String processedPersonName;

    @ApiModelProperty(value = "处理人id.")
    private String processedPersonId;

    @ApiModelProperty(value = "处理图片.")
    private String processedPictureUrl;

    @ApiModelProperty(value = "处理描述.")
    private String processedDesc;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50.")
    private Integer status;

    @ApiModelProperty(value = "工单状态集合.")
    private List<Integer> statusList;

    @ApiModelProperty(value = "指派人id.")
    private String assignPersonId;

    @ApiModelProperty(value = "指派人id名称.")
    private String assignPersonName;

    @ApiModelProperty(value = "退回原因.")
    private String returnReason;

    @ApiModelProperty(value = "关闭原因.")
    private String closeReason;

    @ApiModelProperty(value = "满意度.")
    private Integer satisfaction;

    @ApiModelProperty(value = "评价.")
    private String evaluateContent;

    @ApiModelProperty(value = "告警时间.")
    private Date alarmTime;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Boolean deleted;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人id.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "生成开始时间.")
    // 接收前端参数时的转换格式（核心）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "生成结束时间.")
    // 接收前端参数时的转换格式（核心）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "空间位置id")
    private String spaceId;

    @ApiModelProperty(value = "超时状态")
    private Integer outStatus;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    private Integer dispatchType;

    @ApiModelProperty(value = "分配人名称.")
    private String allotUname;

    @ApiModelProperty(value = "是否转派.")
    private Boolean transferFlag;

    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIdList;
}
