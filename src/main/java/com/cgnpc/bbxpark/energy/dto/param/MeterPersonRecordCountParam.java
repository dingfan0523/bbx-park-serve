
package com.cgnpc.bbxpark.energy.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/***
 * @Description 抄表人工抄表记录统计入参数据模型
 * @author huangyongtao
 * @date 2025/4/21 9:16
 */
@Data
public class MeterPersonRecordCountParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "抄表值.")
    private BigDecimal readingValue;

    @ApiModelProperty(value = "统计时间.")
    private Date countTime;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
