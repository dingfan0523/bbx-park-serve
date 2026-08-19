package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 巡更点入参数据模型
 */
@Data
public class PatrolPointParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3016320192902446268L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @Length(max = 64)
    @ApiModelProperty(value = "名称.")
    @NotBlank(message = "名称不能为空")
    private String name;

    @Length(max = 64)
    @ApiModelProperty(value = "编码.")
    @NotBlank(message = "编码不能为空")
    private String code;

    @ApiModelProperty(value = "类型（10：安保；20：保洁；30：消控；40：环境；50：设备）.")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "方式（10：拍照；20：其他）.")
    @NotNull(message = "方式不能为空")
    private Integer way;

    @ApiModelProperty(value = "位置id.")
    private Long spaceId;

    @Length(max = 200)
    @ApiModelProperty(value = "巡更要求.")
    @NotBlank(message = "巡更要求不能为空")
    private String remark;

    @ApiModelProperty(value = "重点检查(0->否;1->是).")
    @NotNull(message = "重点检查不能为空")
    private Integer keyPoint = 0;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status = 1;
}
