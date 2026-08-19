package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnreadParam {

    @ApiModelProperty(value = "用户标识.")
    private String userId;

    private Long tenantId;
}
