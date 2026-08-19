package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 园区空间校验参数模型
 *
 * @author dingfan
 * @date 2024/7/1 17:08
 */
@Data
public class ParkSpaceCheckNameParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4587033727007666635L;

    @ApiModelProperty(value = "空间标识(仅编辑空间时传)")
    private Long id;
    @ApiModelProperty(value = "所属空间id")
    @NotNull
    private Long parentSpaceId;
    @ApiModelProperty(value = "空间名称.")
    @NotNull
    private String spaceName;
}
