
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class RestaurantListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4645851554890246716L;

    @ApiModelProperty(value = "餐厅名称.")
    private String name;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "状态(1->启用;0->禁用).")
    private Integer status;

    private Long tenantId;

    private List<Long> ids;
}
