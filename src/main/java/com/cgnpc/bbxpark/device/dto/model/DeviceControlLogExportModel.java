
package com.cgnpc.bbxpark.device.dto.model;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 设备控制日志导出模型
 * @author huangyongtao
 * @date 2025/8/5 17:29
 */
@Data
public class DeviceControlLogExportModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "姓名.")
    @ExcelProperty(value = "操作人", index = 1)
    private String name;
    @ApiModelProperty(value = "操作时间")
    @ExcelProperty(value = "操作时间", index = 2)
    private String createTimeStr;
    @ApiModelProperty(value = "设备名称.")
    @ExcelProperty(value = "操作设备", index = 3)
    private String deviceName;
    @ApiModelProperty(value = "操作渠道.")
    @ExcelProperty(value = "操作渠道", index = 4)
    private String channelStr;
    @ApiModelProperty(value = "操作结果.")
    @ExcelProperty(value = "操作结果", index = 5)
    private String resultStr;
}
