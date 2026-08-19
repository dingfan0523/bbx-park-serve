package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 菜品排班详情
 * @author dingfan
 * @date 2024/7/22 15:29
 */
@Data
public class DishesScheduleDetailModel extends DishesScheduleModel{
    @ApiModelProperty(value = "评价集合")
    private List<DishesEvaluateModel> evaluateList;
}
