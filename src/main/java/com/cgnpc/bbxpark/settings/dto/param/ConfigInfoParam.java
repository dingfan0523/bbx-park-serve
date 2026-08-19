package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
@Data
public class ConfigInfoParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4056246655260925915L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "系统信息标识.")
    private Long id;

    @Length(max = 50)
    @Size(min = 1)
    @NotBlank(groups = InsertGroup.class)
    @ApiModelProperty(value = "编码.", required = true)
    private String code;

    @Length(max = 200)
    @ApiModelProperty(value = "描述.")
    private String description;

    @Length(max = 50)
    @ApiModelProperty(value = "文本.")
    private String label;

    @NotBlank(groups = InsertGroup.class)
    @Length(max = 20)
    @Size(min=1)
    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "状态，0正常1禁用.", required = true)
    @Max(1)
    @Min(0)
    private Integer status;

    @ApiModelProperty(value = "类型，0系统配置1自定义配置.")
    @Max(1)
    @Min(0)
    private Integer type;

    @NotBlank(groups = InsertGroup.class)
    @Size(min=1)
    @ApiModelProperty(value = "值.")
    private String value;

    @ApiModelProperty(value = "系统信息标识集合")
    private List<Long> ids;
}
