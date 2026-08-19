package com.cgnpc.bbxpark.message.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MessageNoticeModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4142637936018027361L;

    @ApiModelProperty(value = "通知公告标识.")
    private Long id;

    @ApiModelProperty(value = "类型标识.")
    private Long typeId;

    @ApiModelProperty(value = "消息标识.")
    private Long msgId;

    @ApiModelProperty(value = "通知公告类型，0通知1公告.")
    private Integer type;

    @ApiModelProperty(value = "优先级，0低1中2高.")
    private Integer priority;

    @ApiModelProperty(value = "发布级别，0系统级1组织级2部门级3租户级4应用级.")
    private Integer level;

    @ApiModelProperty(value = "有效状态，0有效1无效2已过期.")
    private Integer effectiveStatus;

    @ApiModelProperty(value = "有效日期.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date effectiveDate;

    @ApiModelProperty(value = "发布时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date publishTime;

    @ApiModelProperty(value = "创建用户昵称.")
    private String nickName;

    @ApiModelProperty(value = "摘要.")
    private String summary;

    @ApiModelProperty(value = "类型名称.")
    private String typeName;

    @ApiModelProperty(value = "标题.")
    private String title;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "通知对象，0全员4组织7租户.")
    private Integer receiverScope;

    /**
     *消息状态，0未发布1已发布2已撤销3已删除.
     **/
    @ApiModelProperty(value = "消息状态，0未发布1已发布2已撤销3已删除")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "发布者id")
    private String publisherId;

    @ApiModelProperty(value = "发布者名称")
    private String publisherName;

    @ApiModelProperty(value = "接收者id")
    private String receiverId;

    @ApiModelProperty(value = "创建者名称.")
    private String creatorName;

    @ApiModelProperty(value = "修改者名称.")
    private String updatorName;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;


    @ApiModelProperty(value = "修改时间.")
    private Date updateTime;

    /**
     * 发送设置，1立即发送、2定时发送
     */
    private Long sendingSettings;
    /**
     * 封面图片地址
     */
    private String cover;

    private String userNames;
}
