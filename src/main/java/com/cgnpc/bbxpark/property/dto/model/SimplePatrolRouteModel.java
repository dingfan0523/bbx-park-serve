
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SimplePatrolRouteModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3496644361583115379L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "路线名称.")
    private String name;

    @ApiModelProperty(value = "路线类型（10：安保路线；20：保洁路线；30：消控路线；40：环境路线；）.")
    private Integer type;

    @ApiModelProperty(value = "路线等级（10：重要；20：一般）.")
    private Integer level;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status = 1;

    @ApiModelProperty(value = "删除状态(0->已删;1->未删).")
    private Integer deleted = 1;
}
