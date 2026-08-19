
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class AppDishesScheduleModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3311741091516297631L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐线名称")
    private String mealLineName;

    @ApiModelProperty(value = "菜品名称.")
    private String name;

    @ApiModelProperty(value = "菜品图片.")
    private String imageUrl;

    @ApiModelProperty(value = "类别(字典).")
    private String type;

    @ApiModelProperty(value = "单价.")
    private String price;

    @ApiModelProperty(value = "克重.")
    private String weight;

    @ApiModelProperty(value = "辣度建议(0,1,2,3,4,5).")
    private Integer pungencyDegree;

    @ApiModelProperty(value = "原料信息.")
    private String information;

    @ApiModelProperty(value = "满意度")
    private Double satisfaction;

    @ApiModelProperty(value = "评价总数")
    private Integer evaluateTotal;
}
