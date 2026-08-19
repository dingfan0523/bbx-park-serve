
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class DishesScheduleImportParam  {



    @ApiModelProperty(value = "出品日期(date).")
    @ExcelProperty(value = {"*出品日期"}, index = 0)
    private String productionDate;

    @Length(max = 64)
    @ApiModelProperty(value = "用餐时间(字典).")
    @ExcelProperty(value = {"*用餐时间"}, index = 1)
    private String mealTime;

    @Length(max = 64)
    @ApiModelProperty(value = "菜品名称.")
    @ExcelProperty(value = {"*菜品名称"}, index = 2)
    private String name;

    @Length(max = 32)
    @ApiModelProperty(value = "类别(字典).")
    @ExcelProperty(value = {"*类别"}, index = 3)
    private String type;

    @Length(max = 255)
    @ApiModelProperty(value = "原料信息.")
    @ExcelProperty(value = {"原料信息"}, index = 4)
    private String information;

    @ApiModelProperty(value = "克重.")
    @ExcelProperty(value = {"克重(g)"}, index = 5)
    private String weight;

    @ApiModelProperty(value = "单价.")
    @ExcelProperty(value = {"*单价(元)"}, index = 6)
    private String price;

    @ApiModelProperty(value = "辣度建议(0,1,2,3,4,5).")
    @ExcelProperty(value = {"辣度建议（0-5）"}, index = 7)
    private String pungencyDegree;

    @ExcelIgnore
    @ApiModelProperty(value = "行数.")
    private String number;

    @ExcelIgnore
    @ApiModelProperty(value = "错误信息.")
    private String errMessage;

}
