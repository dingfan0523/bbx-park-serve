package com.cgnpc.bbxpark.energy.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * @Description 抄表记录支路能耗概览模型
 * @author huangyongtao
 * @date 2025/4/22 13:53
 */
@Data
public class MeterRecordBranchCountParam {

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "本用量")
    private Double value = 0.00;

    @ApiModelProperty(value = "上用量")
    private Double lastValue = 0.00;

    @ApiModelProperty(value = "去年本用量")
    private Double lastYearValue = 0.00;

}
