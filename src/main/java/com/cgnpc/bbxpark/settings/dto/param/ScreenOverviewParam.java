
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class ScreenOverviewParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class, message = "id不能为空")
    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "模块名称.")
    private String modelName;

    @ApiModelProperty(value = "模块类型.")
    private String modelType;

    @ApiModelProperty(value = "模块数据:json字符串")
    private String modelData;
}