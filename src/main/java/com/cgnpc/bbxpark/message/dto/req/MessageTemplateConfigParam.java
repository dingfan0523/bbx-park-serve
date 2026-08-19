
package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
/***
 * @Description 消息模版配置入参数据模型
 * @author huangyongtao
 * @date 2024/10/24 16:33
 */
@Data
public class MessageTemplateConfigParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模版id.")
    private Long templateId;

    @Length(max = 30)
    @ApiModelProperty(value = "类型;person：人员；role：角色.")
    private String type;

    @ApiModelProperty(value = "推送id.")
    private String pushId;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

}
