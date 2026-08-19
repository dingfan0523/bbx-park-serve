package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 巡检点入参数据模型
 */
@Data
public class InspectionPointParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3345906629197940734L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @Length(max = 64)
    @ApiModelProperty(value = "名称.")
    @NotBlank( message = "名称不能为空")
    private String name;

    @Length(max = 64)
    @ApiModelProperty(value = "编码.")
    @NotBlank(message = "编码不能为空")
    private String code;

    @ApiModelProperty(value = "位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "设备ID;多个以英文逗号隔开，例如：1,2,3.")
    private String deviceId;

    @Length(max = 200)
    @ApiModelProperty(value = "巡检要求.")
    @NotBlank(message = "巡检要求不能为空")
    private String remark;
}
