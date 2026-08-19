package com.cgnpc.bbxpark.energy.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @create zhaoshuo
 * @time 2025/4/24
 * @desc 支路能耗流向图查询参数
 */
@Data
public class BranchEnergyFlowQueryParam implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    @ApiModelProperty(value = "支路类型(electricity-电water-水gas-燃气)")
    private String branchType ;

    @ApiModelProperty(value = "读数来源(person:人工抄表 auto:自动上报)")
    private String meterMethod;

    @ApiModelProperty(value = "查询周期类型(1:月 2:年)")
    private Integer queryType;

    @ApiModelProperty(value = "数据开始时间")
    private Date startDate;

    @ApiModelProperty(value = "数据结束时间")
    private Date endDate;
}
