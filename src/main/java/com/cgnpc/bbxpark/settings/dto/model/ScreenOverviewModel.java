
package com.cgnpc.bbxpark.settings.dto.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class ScreenOverviewModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "模块名称.")
    private String modelName;

    @ApiModelProperty(value = "模块类型.")
    private String modelType;

    @ApiModelProperty(value = "模块数据")
    private JSONObject modelData;
}