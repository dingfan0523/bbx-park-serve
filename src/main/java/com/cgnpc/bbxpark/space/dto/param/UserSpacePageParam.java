package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户与空间访问权限分页参数模型
 */
@Data
public class UserSpacePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4367943853838912249L;

    @ApiModelProperty(value = "标识.")
    private Long id;

    @ApiModelProperty(value = "用户ID.")
    private String userId;

    @ApiModelProperty(value = "空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "乐观锁.")
    private String revision;
}
