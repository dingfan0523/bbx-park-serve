
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
public class ClickVolumeParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4239332496064453682L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键标识.")
    private Long id;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @NotBlank(groups = InsertGroup.class)
    @Length(max = 256)
    @ApiModelProperty(value = "菜单编码.")
    private String menuCode;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "访问总计.")
    private Integer visitCount = 1;

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
