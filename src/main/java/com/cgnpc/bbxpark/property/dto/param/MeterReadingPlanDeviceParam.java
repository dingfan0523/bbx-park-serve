
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 抄表计划设备参数据模型
 * @author huangyongtao
 * @date 2025/10/30 16:17
 */
@Data
public class MeterReadingPlanDeviceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "空间位置id;多个以英文，逗号隔开.")
    private String spaceId;

    @ApiModelProperty(value = "抄表类型;water：水表；electricity：电表；gas：燃气表.")
    private String readingType;
}
