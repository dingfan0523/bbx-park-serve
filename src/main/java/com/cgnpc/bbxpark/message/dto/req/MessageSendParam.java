package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 消息发送参数
 * @author dingfan
 * @version 1.0
 * @date 2024/10/25 9:27
 */
@Data
public class MessageSendParam implements Serializable {
    @ApiModelProperty(value = "模板code")
    private String templateCode;
    @ApiModelProperty(value = "业务id")
    private Long businessId;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty(value = "接收者id集合")
    private Set<String> receivers;
    @ApiModelProperty(value = "动态参数")
    private Map<String,String> variables;
}
