
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class CompartmentComboParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3923367386915994857L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "包间id.")
    private Long compartmentId;

    @ApiModelProperty(value = "套餐id.")
    private Long comboId;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "id集合.")
    private List<Long> ids;
}
