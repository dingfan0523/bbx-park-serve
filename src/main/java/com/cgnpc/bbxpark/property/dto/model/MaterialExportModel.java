package com.cgnpc.bbxpark.property.dto.model;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 材料业务数据导出模型
 * @author huangyongtao
 * @date 2025/9/22 15:48
 */
@Data
public class MaterialExportModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "材料名称.")
    @ExcelProperty(value = "材料名称", index = 1)
    private String materialName;

    @ApiModelProperty(value = "材料编码.")
    @ExcelProperty(value = "材料编码", index = 2)
    private String materialCode;

    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材）.")
    @ExcelProperty(value = "材料类型", index = 3)
    private String materialTypeStr;

    @ApiModelProperty(value = "库存数量.")
    @ExcelProperty(value = "库存数量", index = 4)
    private Integer stockQuantity;

    @ApiModelProperty(value = "库存状态;（1：库存充足；2：库存不足；3：缺货）.")
    @ExcelProperty(value = "库存状态", index = 5)
    private String stockStatusStr;

    @ApiModelProperty(value = "备注.")
    @ExcelProperty(value = "材料描述", index = 6)
    private String remark;

    @ApiModelProperty(value = "创建人名称.")
    @ExcelProperty(value = "创建人", index = 7)
    private String creatorUname;

    @ApiModelProperty(value = "创建时间.")
    @ExcelProperty(value = "创建时间", index = 8)
    private String createTimeStr;


}
