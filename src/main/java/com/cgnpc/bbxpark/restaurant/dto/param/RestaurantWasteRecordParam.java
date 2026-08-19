
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RestaurantWasteRecordParam{

    @ApiModelProperty(value = "餐厅名称.")
    @ExcelProperty(value = "餐厅", index = 2)
	private String name;

    @ApiModelProperty(value = "垃圾类型.")
    @ExcelProperty(value = "废弃物种类", index = 4)
	private String type;

    @ApiModelProperty(value = "数量.")
    @ExcelProperty(value = "数量（KG）", index = 5, converter = StringToDoubleConverter.class)
    private Double quantity;

    @ApiModelProperty(value = "处理时间.")
    @ExcelProperty(value = "处理时间", index = 6)
    private String handleTimeStr;

    @ApiModelProperty(value = "处理单位.")
    @ExcelProperty(value = "处理单位", index = 7)
	private String handleUnit;
}
