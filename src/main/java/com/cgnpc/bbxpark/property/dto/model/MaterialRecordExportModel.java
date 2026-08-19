package com.cgnpc.bbxpark.property.dto.model;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 材料记录业务数据导出模型
 * @author huangyongtao
 * @date 2025/9/22 15:49
 */
@Data
public class MaterialRecordExportModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录类型;（1：入库；2：出库）.")
    @ExcelProperty(value = "出入库类型", index = 1)
    private String recordTypeStr;

    @ApiModelProperty(value = "入库单号.")
    @ExcelProperty(value = "入库编号", index = 2)
    private String inboundNo;

    @ApiModelProperty(value = "入库数量.")
    @ExcelProperty(value = "数量", index = 3)
    private Integer quantity;

    @ApiModelProperty(value = "当前库存.")
    @ExcelProperty(value = "当前库存", index = 4)
    private Integer currentStock;

    @ApiModelProperty(value = "数据来源;（1：手动入库；2：工单维修）.")
    @ExcelProperty(value = "数据来源", index = 5)
    private String dataSourceStr;

    @ApiModelProperty(value = "创建人名称.")
    @ExcelProperty(value = "操作人", index = 6)
    private String creatorUname;

    @ApiModelProperty(value = "创建时间.")
    @ExcelProperty(value = "操作时间", index = 7)
    private String createTimeStr;

    @ApiModelProperty(value = "备注.")
    @ExcelProperty(value = "备注", index = 8)
    private String remark;

}
