
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @value 物业分组排班计划入参数据模型
 * @author huangyongtao
 * @date 2025/9/28 11:50
 */
@Data
public class PropertySchedulePlanParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "物业分组id.")
    private Long scheduleId;

    @Length(max = 10)
    @ApiModelProperty(value = "周期类型;month：月；week：周.")
    private String periodType;

    @Length(max = 100)
    @ApiModelProperty(value = "周期的标识;多个以英文逗号隔开，例如：1,2,3.")
    private String periodSign;

    @ApiModelProperty(value = "周期的标识集合")
    private List<Integer> periodSigns;

    @ApiModelProperty(value = "周期的开始时间.")
    private Date periodStartTime;

    @ApiModelProperty(value = "周期的结束时间.")
    private Date periodEndTime;

    @ApiModelProperty(value = "计划开始的时间.")
    private Date planStartTime;

    @ApiModelProperty(value = "计划结束的时间.")
    private Date planEndTime;
}
