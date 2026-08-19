
package com.cgnpc.bbxpark.energy.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/***
 * @Description 抄表自动上报记录统计业务数据模型
 * @author huangyongtao
 * @date 2025/4/18 17:29
 */
@Data
public class MeterAutoRecordCountModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "抄表值.")
    private BigDecimal readingValue;

    @ApiModelProperty(value = "统计时间.")
    private Date countTime;

    @ApiModelProperty(value = "能耗.")
    private BigDecimal energyConsumption;

}
