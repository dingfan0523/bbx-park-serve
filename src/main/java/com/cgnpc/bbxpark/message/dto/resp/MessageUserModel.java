package com.cgnpc.bbxpark.message.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MessageUserModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3214046798162048786L;

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

    @ApiModelProperty(value = "回复内容.")
    private String replyContent;

    @ApiModelProperty(value = "状态，0正常1删除.")
    private Integer status;

    @ApiModelProperty(value = "创建时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "修改时间.")
    private Date updateTime;

    @ApiModelProperty(value = "修改者.")
    private String updatorId;

    @ApiModelProperty(value = "用户昵称.")
    private String nickName;

    @ApiModelProperty(value = "类型id.")
    private Long typeId;
    @ApiModelProperty(value = "类型名称.")
    private String typeName;

    @ApiModelProperty(value = "标题.")
    private String title;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "发布者.")
    private String publisherName;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "发布者.")
    private String summary;

    @ApiModelProperty(value = "图片地址.")
    private String cover;

    @ApiModelProperty(value = "发布时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date publishTime;


    @ApiModelProperty(value = "是否需要阅读反馈，0不需要1需要.")
    private Integer needRead;

    @ApiModelProperty(value = "是否需要签收确认，0不需要1需要.")
    private Integer needConfirm;

    @ApiModelProperty(value = "是否需要回复，0不需要1需要.")
    private Integer needReply;

    @ApiModelProperty(value = "是否需要办理，0不需要1需要.")
    private Integer needHandle;

    @ApiModelProperty(value = "能否删除，0可以1不可以.")
    private Integer canDelete;

    @ApiModelProperty(value = "能否定制通知配置，0可以1不可以.")
    private Integer canNotifications;

    @ApiModelProperty(value = "能否全员广播消息，0可以1不可以.")
    private Integer canBroadcast;

    @ApiModelProperty(value = "业务办理地址.")
    private String linkUrl;

    @ApiModelProperty(value = "创建者名称.")
    private String creatorName;

    @ApiModelProperty(value = "修改者名称.")
    private String updatorName;

    @ApiModelProperty(value = "消息类型，0通知公告1安全须知.")
    private Long type;

    private Long userMsgId;

    private Long businessId;

    private Integer typeLabel;

}
