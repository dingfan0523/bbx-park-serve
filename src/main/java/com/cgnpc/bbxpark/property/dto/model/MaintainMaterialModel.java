package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 维保材料业务数据模型
 * @author huangyongtao
 * @date 2025/10/16 15:20
 */
@Data
public class MaintainMaterialModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "维保id.")
    private Long maintainId;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @ApiModelProperty(value = "材料数量.")
    private Integer materialNum;

}
