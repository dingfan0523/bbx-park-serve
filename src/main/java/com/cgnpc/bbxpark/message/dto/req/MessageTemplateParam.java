
package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/***
 * @Description 消息模版入参数据模型
 * @author huangyongtao
 * @date 2024/10/24 16:38
 */
@Data
public class MessageTemplateParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @NotBlank(message = "模版编码不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "模版编码.")
    private String code;

    @NotBlank(message = "消息名称不能为空")
    @Length(max = 10)
    @ApiModelProperty(value = "消息标题.")
    private String title;

    @NotBlank(message = "消息类型不能为空")
    @Length(max = 30)
    @ApiModelProperty(value = "消息类型;restaurant：智慧餐厅；order：智慧工单；complaint：投诉建议；meeting：智慧会议；alarm：告警消息.")
    private String type;

    @NotBlank(message = "消息内容不能为空")
    @Length(max = 255)
    @ApiModelProperty(value = "消息内容.")
    private String content;

    @ApiModelProperty(value = "模版状态;0：启用；1：禁用.")
    private Integer status;

    @Length(max = 100)
    @ApiModelProperty(value = "推送渠道;多个以英文逗号隔开 info：站内信；ding：钉钉；sms：短信；mail:邮件.")
    private String pushChannel;

    @ApiModelProperty(value = "推送渠道集合")
    private List<String> pushChannelList;

    @ApiModelProperty(value = "是否推送;0：启用；1：禁用.")
    private Integer pushFlag;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "角色id集合")
    private List<String> roleIds;

    @ApiModelProperty(value = "人员id集合")
    private List<String> personIds;

}
