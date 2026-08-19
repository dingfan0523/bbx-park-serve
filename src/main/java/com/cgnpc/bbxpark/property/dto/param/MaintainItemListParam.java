package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 维保项目列表参数模型
 * @author huangyongtao
 * @date 2025/10/16 16:23
 */
@Data
public class MaintainItemListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "维保id.")
    private Long maintainId;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "任务组.")
    private Integer taskGroup;
}
