package com.cgnpc.bbxpark.message.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MessageUserParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3075189076864855040L;

    @NotNull//(groups = UpdateGroup.class)
    @ApiModelProperty(value = "用户消息标识.")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "用户标识.")
    private String userId;

    @NotNull
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

    @ApiModelProperty(value = "状态，0正常1删除.")
    private Integer status;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "修改时间.")
    private Date updateTime;

    @ApiModelProperty(value = "修改者.")
    private String updatorId;

    @ApiModelProperty(value = "用户消息标识集合.")
    private List<Long> ids;


    @ApiModelProperty(value = "标题.")
    private String title;

    @ApiModelProperty(value = "类型标识.")
    private Long typeId;
}
