
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @value 查看物业排班日历入参数据模型
 * @author huangyongtao
 * @date 2025/9/28 11:50
 */
@Data
public class PropertyDatePlanParam implements Serializable {

    @ApiModelProperty(value = "开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "结束时间.")
    private Date endTime;

    @ApiModelProperty(value = "物业分组id集合.")
    private List<Long> ids;
}
