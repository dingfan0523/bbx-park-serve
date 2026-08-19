
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class DishesScheduleListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4906646899852549276L;

    @ApiModelProperty(value = "餐厅id.")
    @NotNull(groups = Default.class,message = "餐厅id不能为空")
    private Long restaurantId;

    @ApiModelProperty(value = "餐线id.")
    private Long mealLineId;

    @ApiModelProperty(value = "餐线id集合.")
    private List<Long> mealLineIds;

    @ApiModelProperty(value = "菜品名称.")
    private String name;

    @ApiModelProperty(value = "类别(字典).")
    private String type;

    @ApiModelProperty(value = "用餐时间(字典).")
    private String mealTime;

    @ApiModelProperty(value = "出品日期")
    private Date productionDate;

    @ApiModelProperty(value = "开始日期(出品日期范围查询)")
    private Date startDate;

    @ApiModelProperty(value = "结束日期(出品日期范围查询)")
    private Date endDate;

    @ApiModelProperty(value = "状态(1->上架;0->下架).")
    private Integer status;
}
