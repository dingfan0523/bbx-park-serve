
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 菜品排班入参数据模型
 * @author dingfan
 * @date 2024/7/18
 */
@Data
public class DishesScheduleParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4787229943722568211L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    @NotNull(groups = {InsertGroup.class,UpdateGroup.class},message = "餐厅id不能为空")
    private Long restaurantId;

    @ApiModelProperty(value = "餐线id.")
    @NotNull(groups = {InsertGroup.class,UpdateGroup.class},message = "餐线id不能为空")
    private Long mealLineId;

    @Length(max = 64)
    @ApiModelProperty(value = "菜品名称.")
    @NotNull(groups = {InsertGroup.class,UpdateGroup.class},message = "菜品名称不能为空")
    @Length(max = 30,message = "菜品名称不能超过30字")
    private String name;

    @Length(max = 32)
    @ApiModelProperty(value = "类别(字典).")
    @NotNull(groups = {InsertGroup.class,UpdateGroup.class},message = "请选择类别")
    private String type;

    @Length(max = 64)
    @ApiModelProperty(value = "用餐时间(字典).")
    @NotNull(groups = {InsertGroup.class,UpdateGroup.class},message = "请选择用餐时间")
    private String mealTime;

    @ApiModelProperty(value = "出品日期")
    @NotNull(groups = {InsertGroup.class,UpdateGroup.class},message = "请选择出品日期")
    private Date productionDate;

    @ApiModelProperty(value = "单价.")
    @NotNull(groups = {InsertGroup.class,UpdateGroup.class},message = "单价不能为空")
    private BigDecimal price;

    @ApiModelProperty(value = "克重.")
    private String weight;

    @ApiModelProperty(value = "辣度建议(0,1,2,3,4,5).")
    private Integer pungencyDegree;

    @Length(max = 200,message = "原料信息不能超过200字")
    @ApiModelProperty(value = "原料信息.")
    private String information;
}
