package com.cgnpc.bbxpark.message.dto.req;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MessageNoticeParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928377745128314045L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "通知公告标识.")
    private Long id;

    @ApiModelProperty(value = "类型标识.")
    private Long typeId;

    @Length(max = 100)
    @ApiModelProperty(value = "消息摘要.")
    private String summary;

    @ApiModelProperty(value = "接收者范围，0全员1特定用户4组织7租户.")
    private Integer receiverScope;

    @ApiModelProperty(value = "接收者标识.")
    private String receiverId;

    @Length(max = 200)
    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "消息标识.")
    private Long msgId;

    @ApiModelProperty(value = "通知公告类型，0安全须知 1公告.")
    private Integer type;

    @ApiModelProperty(value = "优先级，0低1中2高.")
    private Integer priority;

    @ApiModelProperty(value = "发布级别，0系统级1组织级2部门级3租户级4应用级.")
    private Integer level;

    @ApiModelProperty(value = "有效状态，0有效1无效2已过期.")
    private Integer effectiveStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "有效日期.")
    private Date effectiveDate;

    @ApiModelProperty(value = "通知公告标识集合.")
    private List<Long> ids;

    @ApiModelProperty(value = "用户昵称.")
    private String nickName;

    @ApiModelProperty(value = "类型名称.")
    private String typeName;

    @ApiModelProperty(value = "标题.")
    private String title;

    @ApiModelProperty(value = "内容.")
    private String content;


    /**
     *创建时间.
     **/
    private Date createTime;
    /**
     *创建者.
     **/
    private String creatorId;
    /**
     *修改时间.
     **/
    private Date updateTime;
    /**
     *修改者.
     **/
    private String updatorId;

    /**
     * 发送设置，1立即发送、2定时发送
     */
    private Long sendingSettings;

    private Long publisher;

    /**
     * 定时发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date publishTime;

    private Integer status;

    /**
     * 封面图片地址
     */
    private String cover;

    /**
     * 业务标识
     */
    private Long businessId;

    private Long tenantId;
}
