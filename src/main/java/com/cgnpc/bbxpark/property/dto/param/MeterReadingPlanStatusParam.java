
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 抄表计划管理入参数据模型
 * @author huangyongtao
 * @date 2025/3/25 16:17
 */
@Data
public class MeterReadingPlanStatusParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "启用状态;1->是;0->否.")
    private Integer status;
}
