package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 巡更路线入参数据模型
 */
@Data
public class PatrolRouteParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4382224064542292379L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @Length(max = 64)
    @ApiModelProperty(value = "路线名称.")
    @NotBlank(message = "路线名称不能为空")
    private String name;

    @ApiModelProperty(value = "路线类型（10：安保路线；20：保洁路线；30：消控路线；40：环境路线；）.")
    @NotNull(message = "路线类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "路线等级（10：重要；20：一般）.")
    @NotNull(message = "路线等级不能为空")
    private Integer level;

    @ApiModelProperty(value = "是否有序(0->否;1->是).")
    @NotNull(message = "是否有序不能为空")
    private Integer sequence = 0;

    @ApiModelProperty(value = "路线距离（km）.")
    private Double distance;

    @ApiModelProperty(value = "预计用时(分钟).")
    private Double useTime;

    @Length(max = 200)
    @ApiModelProperty(value = "路线描述.")
    private String remark;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status = 1;
}
