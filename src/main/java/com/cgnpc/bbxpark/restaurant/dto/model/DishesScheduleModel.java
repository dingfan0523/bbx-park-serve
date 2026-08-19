
package com.cgnpc.bbxpark.restaurant.dto.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class DishesScheduleModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3311741091516297631L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "餐线id.")
    private Long mealLineId;

    @ApiModelProperty(value = "菜品名称.")
    private String name;

    @ApiModelProperty(value = "菜品图片.")
    private String imageUrl;

    @ApiModelProperty(value = "类别(字典).")
    private String type;

    @ApiModelProperty(value = "餐线名称")
    private String mealLineName;

    @ApiModelProperty(value = "用餐时间(字典).")
    private String mealTime;

    @ApiModelProperty(value = "出品日期(date).")
    private Date productionDate;

    @ApiModelProperty(value = "星期（1,2,3,4,5,6,7->对应周一到周日）.")
    private String week;

    @ApiModelProperty(value = "单价.")
    private String price;

    @ApiModelProperty(value = "克重.")
    private String weight;

    @ApiModelProperty(value = "辣度建议(0,1,2,3,4,5).")
    private Integer pungencyDegree;

    @ApiModelProperty(value = "原料信息.")
    private String information;

    @ApiModelProperty(value = "状态(1->上架;0->下架).")
    private Integer status;
}
