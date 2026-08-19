
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/***
 * @value 物业分组排班计划列表参数模型
 * @author huangyongtao
 * @date 2025/9/28 11:49
 */
@Data
public class PropertySchedulePlanListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物业分组id.")
    private Long scheduleId;

    @ApiModelProperty(value = "物业分组id集合.")
    private List<Long> scheduleIds;

    @ApiModelProperty(value = "周期类型;month：月；week：周.")
    private String periodType;

    @ApiModelProperty(value = "周期的标识;多个以英文逗号隔开，例如：1,2,3.")
    private String periodSign;

    @ApiModelProperty(value = "周期的开始时间.")
    private Date periodStartTime;

    @ApiModelProperty(value = "周期的结束时间.")
    private Date periodEndTime;

    @ApiModelProperty(value = "计划开始的时间.")
    private Date planStartTime;

    @ApiModelProperty(value = "计划结束的时间.")
    private Date planEndTime;
}
