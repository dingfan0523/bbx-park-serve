package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息重发参数
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/29 15:57
 */
@Data
public class MessageReSendParam implements Serializable {
    @ApiModelProperty(value = "日志id")
    private Long id;
}
