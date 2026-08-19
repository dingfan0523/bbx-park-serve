package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 维保项目业务数据模型
 * @author huangyongtao
 * @date 2025/10/16 15:19
 */
@Data
public class MaintainItemModel implements Serializable {

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
