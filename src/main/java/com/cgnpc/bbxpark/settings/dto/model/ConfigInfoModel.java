package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ConfigInfoModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4060576273636558393L;

    @NotNull
    @ApiModelProperty(value = "系统信息标识.")
    private Long id;

    @Length(max = 50)
    @ApiModelProperty(value = "编码.")
    private String code;

    @Length(max = 200)
    @ApiModelProperty(value = "描述.")
    private String description;

    @Length(max = 50)
    @ApiModelProperty(value = "文本.")
    private String label;

    @NotBlank
    @Length(max = 20)
    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "状态，0正常1禁用.")
    private Integer status;

    @ApiModelProperty(value = "类型，0系统配置1自定义配置.")
    private Integer type;

    @NotBlank
    @ApiModelProperty(value = "值.")
    private String value;
}
