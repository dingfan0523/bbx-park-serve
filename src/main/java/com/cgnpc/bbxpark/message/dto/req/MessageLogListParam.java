
package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 消息日志列表参数模型
 * @author huangyongtao
 * @date 2024/10/24 16:24
 */
@Data
public class MessageLogListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模版id.")
    private Long templateId;

    @ApiModelProperty(value = "消息标题.")
    private String title;

    @ApiModelProperty(value = "消息类型;restaurant：智慧餐厅；order：智慧工单；complaint：投诉建议；meeting：智慧会议；alarm：告警消息.")
    private String type;

    @ApiModelProperty(value = "状态;0：成功；1：失败.")
    private Integer status;

    @ApiModelProperty(value = "推送渠道;info：站内信；ding：钉钉；sms：短信；mail:邮件.")
    private String pushChannel;

    @ApiModelProperty(value = "发送时间.")
    private Date pushTime;

    @ApiModelProperty(value = "发送时间开始时间")
    private Date pushTimeStart;

    @ApiModelProperty(value = "发送时间结束时间")
    private Date pushTimeEnd;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

}
