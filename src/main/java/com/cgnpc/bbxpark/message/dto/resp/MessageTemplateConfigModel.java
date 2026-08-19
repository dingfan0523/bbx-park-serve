
package com.cgnpc.bbxpark.message.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 消息模版配置业务数据模型
 * @author huangyongtao
 * @date 2024/10/24 16:10
 */
@Data
public class MessageTemplateConfigModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "模版id.")
    private Long templateId;

    @ApiModelProperty(value = "类型;person：人员；role：角色.")
    private String type;

    @ApiModelProperty(value = "推送id.")
    private String pushId;

    @ApiModelProperty(value = "推送名称")
    private String pushName;

    @ApiModelProperty(value = "工号")
    private String staffid;
}
