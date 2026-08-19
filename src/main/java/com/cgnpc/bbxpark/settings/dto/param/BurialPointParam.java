
package com.cgnpc.bbxpark.settings.dto.param;


import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class BurialPointParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3224118224436259240L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键标识.")
    private Long id;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @NotBlank(groups = InsertGroup.class)
    @Length(max = 256)
    @ApiModelProperty(value = "埋点功能编码;例：投诉访问，投诉恢复.")
    private String burialPointFunctionCode;

    @Length(max = 32)
    @ApiModelProperty(value = "事件类型;例：点击事件、浏览事件.")
    private String eventType;

    @ApiModelProperty(value = "触发时间.")
    private Date triggerTime;

    @ApiModelProperty(value = "停留时长.")
    private Integer durationStay;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新者.")
    private Long updaterId;

    @ApiModelProperty(value = "主键标识集合.")
    private List<Long> ids;
}
