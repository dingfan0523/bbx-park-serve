
package com.cgnpc.bbxpark.message.dto.req;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 消息模版配置分页参数模型
 * @author huangyongtao
 * @date 2024/10/24 16:32
 */
@Data
public class MessageTemplateConfigPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模版id.")
    private Long templateId;

    @ApiModelProperty(value = "类型;person：人员；role：角色.")
    private String type;

    @ApiModelProperty(value = "推送id.")
    private Long pushId;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

}
