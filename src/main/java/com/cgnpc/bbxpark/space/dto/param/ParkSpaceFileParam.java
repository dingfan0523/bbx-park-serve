
package com.cgnpc.bbxpark.space.dto.param;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ParkSpaceFileParam implements Serializable {

	private static final long serialVersionUID = 1416677491737981408L;

	@ApiModelProperty(value = "上级空间编码.")
	@ExcelProperty(value = {"*上级空间编码(必填)"}, index = 0)
	private String parentSpaceCode;

	@ApiModelProperty(value = "空间编码.")
	@ExcelProperty(value = {"*空间编码（必填）"}, index = 1)
	private String spaceCode;

	@ApiModelProperty(value = "空间名称.")
	@ExcelProperty(value = {"*空间名称"}, index = 2)
	private String spaceName;

	@ApiModelProperty(value = "排序序号.")
	@ExcelProperty(value = {"排序序号"}, index = 3)
	private String orderCode;

	@ApiModelProperty(value = "失败错误描述.")
	@ExcelProperty(value = {"失败错误描述"}, index = 4)
	private String importErrorDesc;

	@ApiModelProperty(value = "生成编码.")
	private String num;

}
