package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
public class MessageUserRemoveParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3075189076864855040L;

    @NotNull
    @ApiModelProperty(value = "用户标识.userIds")
    private List<Long> userIds;

    @NotNull
    @ApiModelProperty(value = "消息标识msgId.")
    private Long msgId;

}
