package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 园区空间校验参数模型
 * @author dingfan
 * @date 2024/7/1 17:08
 */
@Data
public class ParkSpaceCheckCodeParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4587033727007666635L;

    @ApiModelProperty(value = "空间标识(仅编辑空间时传)")
    private Long id;
    @ApiModelProperty(value = "所属园区ID-租户号.")
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "空间编码.")
    @NotNull
    private String spaceCode;
}
