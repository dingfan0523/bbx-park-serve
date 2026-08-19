
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class DishesEvaluateModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3185593000050172652L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "满意度.")
    private Integer satisfaction;

    @ApiModelProperty(value = "味道(字典)")
    private String taste;

    @ApiModelProperty(value = "评价人id.")
    private String appraiserId;

    @ApiModelProperty(value = "评价人名称.")
    private String appraiserName;

    @ApiModelProperty(value = "匿名状态(0->未匿名;1->匿名).")
    private Integer anonymityStatus;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;
}
