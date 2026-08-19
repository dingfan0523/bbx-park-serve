package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MessageReplyParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3075189076864855040L;

    @NotNull
    @ApiModelProperty(value = "用户消息标识.")
    private Long id;

    @NotNull
    @Length(max = 500)
    @ApiModelProperty(value = "回复内容.")
    private String replyContent;


}
