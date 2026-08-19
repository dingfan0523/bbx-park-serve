
package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 消息模版列表参数模型
 * @author huangyongtao
 * @date 2024/10/24 16:34
 */
@Data
public class MessageTemplateListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模版编码.")
    private String code;

    @ApiModelProperty(value = "消息标题.")
    private String title;

    @ApiModelProperty(value = "消息类型;restaurant：智慧餐厅；order：智慧工单；complaint：投诉建议；meeting：智慧会议；alarm：告警消息.")
    private String type;

    @ApiModelProperty(value = "模版状态;0：启用；1：禁用.")
    private Integer status;

    @ApiModelProperty(value = "推送渠道;多个以英文逗号隔开 info：站内信；ding：钉钉；sms：短信；mail:邮件.")
    private String pushChannel;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

}
