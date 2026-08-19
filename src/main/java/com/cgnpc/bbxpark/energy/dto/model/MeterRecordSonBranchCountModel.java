package com.cgnpc.bbxpark.energy.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * @Description 抄表记录子支路能耗概览模型
 * @author huangyongtao
 * @date 2025/4/22 13:53
 */
@Data
public class MeterRecordSonBranchCountModel {

    @ApiModelProperty(value = "支路名称")
    private String branchName;

    @ApiModelProperty(value = "本用量")
    private Double value = 0d;

    @ApiModelProperty(value = "上用量")
    private Double lastValue = 0d;

    @ApiModelProperty(value = "去年本用量")
    private Double lastYearValue = 0d;

}
