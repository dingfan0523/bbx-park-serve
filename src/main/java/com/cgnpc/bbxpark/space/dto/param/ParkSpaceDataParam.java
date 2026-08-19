
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class ParkSpaceDataParam implements Serializable {


	@ApiModelProperty(value = "解析导入后台文件数据集合")
	private List<ParkSpaceFileParam> parkSpaceFileParamList;

	@ApiModelProperty(value = "批次")
	private String batchCode;

	@ApiModelProperty(value = "excle入文件校验不通过数据集合")
	private List<ParkSpaceImportTemporaryParam> parkSpaceImportTemporaryParams;

}
