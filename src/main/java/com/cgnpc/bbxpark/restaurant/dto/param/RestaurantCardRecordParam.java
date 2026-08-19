
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToIntegerConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RestaurantCardRecordParam{
    @ApiModelProperty(value = "一卡通号.")
    @ExcelProperty(index = 0)
	private String cardNo;

    @ApiModelProperty(value = "姓名.")
    @ExcelProperty(index = 2)
	private String name;

    @ApiModelProperty(value = "餐厅名称.")
    @ExcelProperty(index = 4)
    private String restaurantName;

    @ApiModelProperty(value = "发生时间.")
    @ExcelProperty(index = 6)
	private String occurTimeStr;

    @ApiModelProperty(value = "交易额.")
    @ExcelProperty(index = 10, converter = StringToDoubleConverter.class)
	private Double tradeAmount;

    @ApiModelProperty(value = "账号余额.")
    @ExcelProperty(index = 12, converter = StringToDoubleConverter.class)
	private Double accountBalance;

    @ApiModelProperty(value = "用卡次数.")
    @ExcelProperty(index = 14, converter = StringToIntegerConverter.class)
	private Integer cardUseNum;

    @ApiModelProperty(value = "工号.")
    @ExcelProperty(index = 20)
	private String staff;

    @ApiModelProperty(value = "pos号.")
    @ExcelProperty(index = 24)
	private String pos;
}
