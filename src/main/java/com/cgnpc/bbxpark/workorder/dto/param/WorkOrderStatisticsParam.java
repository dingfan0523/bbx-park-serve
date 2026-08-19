package com.cgnpc.bbxpark.workorder.dto.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @create zhaoshuo
 * @time 2025/4/15
 * @desc 物业工单统计入参
 */
@Data
public class WorkOrderStatisticsParam {

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}
