
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class PatrolRouteModel implements Serializable {

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

    @ApiModelProperty(value = "巡更点数量")
    private Integer pointCount = 0;

    @ApiModelProperty(value = "是否有序(0->否;1->是).")
    private Integer sequence = 0;

    @ApiModelProperty(value = "路线距离（km）.")
    private Double distance;

    @ApiModelProperty(value = "预计用时(分钟).")
    private Double useTime;

    @ApiModelProperty(value = "路线描述.")
    private String remark;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status = 1;
}
