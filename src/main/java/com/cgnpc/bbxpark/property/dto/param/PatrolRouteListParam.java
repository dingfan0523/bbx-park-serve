package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 巡更路线列表参数模型
 */
@Data
public class PatrolRouteListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3721186024204445320L;

    @ApiModelProperty(value = "路线名称.")
    private String name;

    @ApiModelProperty(value = "路线类型（10：安保路线；20：保洁路线；30：消控路线；40：环境路线；）.")
    private Integer type;

    @ApiModelProperty(value = "路线等级（10：重要；20：一般）.")
    private Integer level;

    @ApiModelProperty(value = "是否有序(0->否;1->是).")
    private Integer sequence;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status;

    @ApiModelProperty(value = "主键id集合.")
    private List<Long> ids;
}
