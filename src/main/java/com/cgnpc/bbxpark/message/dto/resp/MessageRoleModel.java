package com.cgnpc.bbxpark.message.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huangyongtao
 * @Description 消息中心的角色信息
 * @date 2024/11/4 14:21
 */
@Data
public class MessageRoleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色标识.")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "状态，0正常1禁用")
    private Short status;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
}
