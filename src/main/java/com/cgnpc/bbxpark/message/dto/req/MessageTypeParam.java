package com.cgnpc.bbxpark.message.dto.req;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MessageTypeParam  extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4767319800963464353L;

    @NotNull//(groups = UpdateGroup.class)
    @ApiModelProperty(value = "类型标识.")
    private Long id;

    @NotBlank//(groups = InsertGroup.class)
    @Length(max = 20)
    @ApiModelProperty(value = "类型名称.")
    private String name;

    @NotBlank//(groups = InsertGroup.class)
    @Length(max = 50)
    @ApiModelProperty(value = "类型编码.")
    private String code;

    @ApiModelProperty(value = "图标.")
    private String icon;

    @ApiModelProperty(value = "类型，0系统1自定义.")
    private Short type;

    @ApiModelProperty(value = "状态，0正常1禁用.")
    private Short status;

    @ApiModelProperty(value = "是否需要阅读反馈，0不需要1需要.")
    private Short needRead;

    @ApiModelProperty(value = "是否需要签收确认，0不需要1需要.")
    private Short needConfirm;

    @ApiModelProperty(value = "是否需要回复，0不需要1需要.")
    private Short needReply;

    @ApiModelProperty(value = "是否需要办理，0不需要1需要.")
    private Short needHandle;

    @ApiModelProperty(value = "能否删除，0可以1不可以.")
    private Short canDelete;

    @ApiModelProperty(value = "能否定制通知配置，0可以1不可以.")
    private Short canNotifications;

    @ApiModelProperty(value = "能否全员广播消息，0可以1不可以.")
    private Short canBroadcast;

    @ApiModelProperty(value = "广播消息人数限制，0或-1表示无限制.")
    private Integer broadcastLimit;

    @Length(max = 200)
    @ApiModelProperty(value = "描述.")
    private String description;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "修改时间.")
    private Date updateTime;

    @ApiModelProperty(value = "修改者.")
    private String updatorId;

    @ApiModelProperty(value = "类型标识集合.")
    private List<Long> ids;
}
