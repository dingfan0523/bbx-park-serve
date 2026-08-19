
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RestaurantInventoryRecordParam {

    @ApiModelProperty(value = "仓库名称.")
    @ExcelProperty(value = "仓库名称", index = 0)
	private String godown;

    @ApiModelProperty(value = "物料大类.")
    @ExcelProperty(value = "物料大类", index = 1)
	private String category;

    @ApiModelProperty(value = "物料中类.")
    @ExcelProperty(value = "物料中类", index = 2)
    private String mediumCategory;

    @ApiModelProperty(value = "物料名称.")
    @ExcelProperty(value = "物料名称", index = 4)
	private String name;

    @ApiModelProperty(value = "库存单位.")
    @ExcelProperty(value = "物料单位", index = 6)
	private String stockUnit;

    @ApiModelProperty(value = "库存数量.")
    @ExcelProperty(value = "库存数量", index = 7, converter = StringToDoubleConverter.class)
	private Double stockQuantity;
}
