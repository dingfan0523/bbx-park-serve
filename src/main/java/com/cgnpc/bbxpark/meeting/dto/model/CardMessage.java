package com.cgnpc.bbxpark.meeting.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页-卡片消息模型
 * @author dingfan
 * @version 1.0
 * @date 2024/10/15 10:05
 */
@Data
@NoArgsConstructor
public class CardMessage implements Serializable {
    @ApiModelProperty(value = "卡片类型")
    private Integer type;
    @ApiModelProperty(value = "卡片名称")
    private String typeName;
    @ApiModelProperty(value = "业务id")
    private Long businessId;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    public CardMessage(Integer type, String typeName, Long businessId, String title, Date startTime, Date endTime) {}
}
