package com.cgnpc.bbxpark.message.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MessageInfoParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3036644831412868929L;

    @NotNull//(groups = UpdateGroup.class)
    @ApiModelProperty(value = "消息标识.")
    private Long id;

    @ApiModelProperty(value = "类型标识.")
    private Long typeId;

    @NotBlank
    @Length(max = 50)
    @ApiModelProperty(value = "消息标题.")
    private String title;

    @Length(max = 100)
    @ApiModelProperty(value = "消息摘要.")
    private String summary;

    @NotBlank
    @Length(max = 65535)
    @ApiModelProperty(value = "消息内容.")
    private String content;

    @ApiModelProperty(value = "模版标识.")
    private Long templateId;

    @ApiModelProperty(value = "模版参数，格式为code、value对象数组.")
    private List<Map> templateParam;

    @Length(max = 250)
    @ApiModelProperty(value = "业务办理地址.")
    private String linkUrl;

    @ApiModelProperty(value = "消息状态，0未发布1已发布2已撤销3已删除.")
    private Integer status;

    @ApiModelProperty(value = "阅读量.")
    private Integer readCount;

    @ApiModelProperty(value = "签收量.")
    private Integer confirmCount;

    @ApiModelProperty(value = "回复量.")
    private Integer replyCount;

    @ApiModelProperty(value = "点赞量.")
    private Integer likeCount;

    @ApiModelProperty(value = "收藏量.")
    private Integer collectCount;

//    @ApiModelProperty(value = "接收者范围，0全员1特定用户2分组3角色4组织5当前部门6当前部门及子部门7租户.")
    @ApiModelProperty(value = "接收者范围，0全员1特定用户4组织7租户.")
    private Integer receiverScope;

    @NotNull
    @ApiModelProperty(value = "接收者标识.")
    private Long receiverId;

    @ApiModelProperty(value = "接收者数量.")
    private Integer receiverCount;

    @ApiModelProperty(value = "发布者，0为系统.")
    private Long publisher;

    @ApiModelProperty(value = "发布时间.")
    private Date publishTime;

    @Length(max = 1000)
    @ApiModelProperty(value = "扩展信息.")
    private String extra;

    @Length(max = 200)
    @ApiModelProperty(value = "描述.")
    private String description;

    @Length(max = 200)
    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "修改时间.")
    private Date updateTime;

    @ApiModelProperty(value = "修改者.")
    private String updatorId;

    @ApiModelProperty(value = "消息标识集合.")
    private List<Long> ids;
}
