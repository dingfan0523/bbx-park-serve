package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangyongtao
 * @Description 消息中心的角色信息
 * @date 2024/11/4 14:21
 */
@Data
public class MessageRoleParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色标识.")
    private Long id;

    @ApiModelProperty(value = "角色标识集合")
    private List<Long> ids;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "状态，0正常1禁用")
    private Integer status;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
}
