package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:20
 */
@Data
@ApiModel(value = "会服人员工作负荷")
public class ServiceStaffWorkload {
    @ApiModelProperty(value = "工号")
    private String staffId;
    @ApiModelProperty(value = "名称")
    private String staffName;
    @ApiModelProperty(value = "服务次数")
    private Long serviceCount;
    @ApiModelProperty(value = "平均评价评分")
    private Double avgScore;
}
