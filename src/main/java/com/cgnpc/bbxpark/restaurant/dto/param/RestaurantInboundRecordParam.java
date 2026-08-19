
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RestaurantInboundRecordParam{

    @ApiModelProperty(value = "仓库名称.")
    @ExcelProperty(value = "仓库", index = 2)
	private String godown;

    @ApiModelProperty(value = "供应商名称.")
    @ExcelProperty(value = "供应商名称", index = 3)
	private String supplier;

    @ApiModelProperty(value = "入库时间.")
    @ExcelProperty(value = "入库日期", index = 4)
    private String inboundTimeStr;

    @ApiModelProperty(value = "物料类型.")
    @ExcelProperty(value ="物料类型", index = 5)
	private String category;

    @ApiModelProperty(value = "物料名称.")
    @ExcelProperty(value = "商品名称", index = 7)
    private String name;

    @ApiModelProperty(value = "入库单位.")
    @ExcelProperty(value = "单位", index = 9)
	private String inboundUnit;

    @ApiModelProperty(value = "税率.")
    @ExcelProperty(index = 12)
    private String inbound123;

    @ApiModelProperty(value = "入库数量.")
    @ExcelProperty(index = 13, converter = StringToDoubleConverter.class)
	private Double inboundQuantity;
}
