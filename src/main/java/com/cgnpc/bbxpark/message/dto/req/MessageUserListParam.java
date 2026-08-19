package com.cgnpc.bbxpark.message.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@Data
public class MessageUserListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4415285885149193284L;

    @ApiModelProperty(value = "用户消息标识.")
    private Long id;

    @ApiModelProperty(value = "用户标识.")
    private String userId;

    @ApiModelProperty(value = "消息标识.")
    private Long msgId;

    @ApiModelProperty(value = "阅读状态，0未读1已读.")
    private Integer readStatus;

    @ApiModelProperty(value = "签收状态，0未确认1已确认.")
    private Integer confirmStatus;

    @ApiModelProperty(value = "是否点赞，0未点赞1已点赞.")
    private Integer likeStatus;

    @ApiModelProperty(value = "是否收藏，0未收藏1已收藏.")
    private Integer collectStatus;

    @ApiModelProperty(value = "地址参数，格式为code、value对象数组.")
    private String linkParam;

    @Length(max = 500)
    @ApiModelProperty(value = "回复内容.")
    private String replyContent;

    @ApiModelProperty(value = "状态，1正常0删除.")
    private Integer status;

    @ApiModelProperty(value = "类型标识.")
    private Long typeId;

    @ApiModelProperty(value = "标题.")
    private String title;
}
