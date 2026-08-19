
package com.cgnpc.bbxpark.invitation.dto.model;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 访客通行记录导出模型
 * @author huangyongtao
 * @date 2025/8/5 17:29
 */
@Data
public class AccessRecordVisitorExportModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "姓名.")
    @ExcelProperty(value = "访客", index = 1)
    private String name;
    @ApiModelProperty(value = "联系方式.")
    @ExcelProperty(value = "联系方式", index = 2)
    private String mobile;
    @ApiModelProperty(value = "通行时间")
    @ExcelProperty(value = "通行时间", index = 3)
    private String createTimeStr;
    @ApiModelProperty(value = "通行方向.")
    @ExcelProperty(value = "通行方向", index = 4)
    private String accessDir;
    @ApiModelProperty(value = "设备名称.")
    @ExcelProperty(value = "通行设备", index = 5)
    private String deviceName;
    @ApiModelProperty(value = "设备位置")
    @ExcelProperty(value = "设备位置", index = 6)
    private String deviceLocation;
    @ApiModelProperty(value = "通行方式.")
    @ExcelProperty(value = "通行方式", index = 7)
    private String accessWay;
}
