
package com.cgnpc.bbxpark.energy.dto.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/***
 * @Description 抄表自动上报记录业务数据模型
 * @author huangyongtao
 * @date 2025/4/18 17:29
 */
@Data
public class MeterRecordExportModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备名称.")
    @ExcelProperty(value = "设备名称", index = 1)
    private String deviceName;

    @ApiModelProperty(value = "抄表编码.")
    @ExcelProperty(value = "设备编号", index = 2)
    private String readingCode;

    @ApiModelProperty(value = "所属空间名称.")
    @ExcelProperty(value = "设备位置", index = 3)
    private String spaceName;

    @ApiModelProperty(value = "抄表值")
    @ExcelIgnore
    private BigDecimal readingValue;

    @ApiModelProperty(value = "抄表值")
    @ExcelProperty(value = "设备读数", index = 4)
    private Double readingValueDouble;

    @ApiModelProperty(value = "创建时间.")
    @ExcelIgnore
    private Date createTime;

    @ApiModelProperty(value = "创建时间.")
    @ExcelProperty(value = "读表时间", index = 5)
    private String createTimeStr;

    @ApiModelProperty(value = "设备类型;（0：智能；1：非智能）.")
    @ExcelIgnore
    private Integer deviceType = 0;

    @ApiModelProperty(value = "设备类型;（1：智能；0：非智能）.")
    @ExcelProperty(value = "设备类型", index = 6)
    private String deviceTypeStr;
}
