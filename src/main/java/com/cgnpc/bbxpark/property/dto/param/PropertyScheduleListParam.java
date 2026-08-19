
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 物业排班列表参数模型
 */
@Data
public class PropertyScheduleListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3582198138624055091L;

    @ApiModelProperty(value = "分组名称.")
    private String name;

    @ApiModelProperty(value = "分组Id集合.")
    private List<Long> ids;

}
