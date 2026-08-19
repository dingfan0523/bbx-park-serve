package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class ConfigPageParam extends CudPageDto {


    @Length(max = 50)
    @ApiModelProperty(value = "编码.")
    private String code;

    @Length(max = 200)
    @ApiModelProperty(value = "描述.")
    private String description;

    @Length(max = 50)
    @ApiModelProperty(value = "文本.")
    private String label;

    @Length(max = 20)
    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "状态，0正常1禁用.")
    @Max(1)
    @Min(0)
    private Integer status;

    @ApiModelProperty(value = "类型，0系统配置1自定义配置.")
    @Max(1)
    @Min(0)
    private Integer type;
}
