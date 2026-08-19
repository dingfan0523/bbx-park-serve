package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户与空间访问权限列表参数模型
 */
@Data
public class UserSpaceListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3182308959679993905L;

    @ApiModelProperty(value = "标识.")
    private Long id;

    @ApiModelProperty(value = "用户ID.")
    private String userId;

    @ApiModelProperty(value = "员工号")
    private String staffId;

    @ApiModelProperty(value = "空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "乐观锁.")
    private String revision;

    @ApiModelProperty(value = "标识集合.")
    private List<Long> ids;
}
