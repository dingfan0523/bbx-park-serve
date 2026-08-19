
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class DocumentViewRecordsParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4335276335773705879L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键标识.")
    private Long id;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "文档类型 userPrivacyAgree:用户隐私协议.")
    private String documentType;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新者.")
    private String updatorId;

    @ApiModelProperty(value = "主键标识集合.")
    private List<Long> ids;
}
