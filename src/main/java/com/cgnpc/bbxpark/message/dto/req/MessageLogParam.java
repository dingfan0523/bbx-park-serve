
package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 消息日志入参数据模型
 * @author huangyongtao
 * @date 2024/10/24 16:28
 */
@Data
public class MessageLogParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "模版id.")
    private Long templateId;

    @Length(max = 10)
    @ApiModelProperty(value = "消息标题.")
    private String title;

    @Length(max = 30)
    @ApiModelProperty(value = "消息类型;restaurant：智慧餐厅；order：智慧工单；complaint：投诉建议；meeting：智慧会议；alarm：告警消息.")
    private String type;

    @Length(max = 255)
    @ApiModelProperty(value = "消息内容.")
    private String content;

    @ApiModelProperty(value = "状态;0：成功；1：失败.")
    private Integer status;

    @Length(max = 100)
    @ApiModelProperty(value = "推送渠道;info：站内信；ding：钉钉；sms：短信；mail:邮件.")
    private String pushChannel;

    @ApiModelProperty(value = "发送时间.")
    private Date pushTime;

    @ApiModelProperty(value = "业务id.")
    private Long businessId;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;
}
