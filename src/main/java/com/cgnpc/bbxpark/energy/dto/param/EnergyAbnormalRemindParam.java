package com.cgnpc.bbxpark.energy.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @create zhaoshuo
 * @time 2025/4/23
 * @desc 能源异常提醒查询参数
 */
@Data
public class EnergyAbnormalRemindParam extends CudPageDto implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;
    @ApiModelProperty(value = "提醒标题")
    private String remindName ;
    @ApiModelProperty(value = "报单状态（0未报单1已报单）")
    private Integer status ;
    @ApiModelProperty(value = "提醒开始时间")
    private Date createTimeStart ;
    @ApiModelProperty(value = "提醒结束时间")
    private Date createTimeEnd ;
}
