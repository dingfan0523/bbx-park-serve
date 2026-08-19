
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class CompartmentTimeParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3709812633559602853L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "包间id.")
    private Long compartmentId;

    @Length(max = 32)
    @ApiModelProperty(value = "用餐类型（字典）.")
    private String type;

    @Length(max = 32)
    @ApiModelProperty(value = "开始时间.")
    private String startTime;

    @Length(max = 32)
    @ApiModelProperty(value = "结束时间.")
    private String endTime;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "id集合.")
    private List<Long> ids;
}
