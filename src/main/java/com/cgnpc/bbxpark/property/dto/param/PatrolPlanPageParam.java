
package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class PatrolPlanPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4714691465107562566L;

    @ApiModelProperty(value = "计划名称.")
    private String planName;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status;

    @ApiModelProperty(value = "物业分组id.")
    private Long scheduleId;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    private Integer dispatchType;

    @ApiModelProperty(value = "计划的周期;1:周期；2：单次.")
    private Integer planPeriod;
}
