
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SimpleSpaceBasicInfoModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3238461757583605392L;

    @ApiModelProperty(value = "id.")
    private Long id;
    @ApiModelProperty(value = "空间编码.")
    private String spaceCode;
    @ApiModelProperty(value = "空间名称.")
    private String spaceName;
    @ApiModelProperty(value = "空间类型.")
    private Integer type;
}
