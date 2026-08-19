
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @value 查看物业排班日历详情数据模型
 * @author huangyongtao
 * @date 2025/9/28 11:47
 */
@Data
public class PropertyDatePlanDetailModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "分组名称.")
    private String name;

    @ApiModelProperty(value = "分组描述.")
    private String remark;

    @ApiModelProperty(value = "人员数量")
    private Long userCount;

    @ApiModelProperty(value = "周期的开始时间.")
    private String periodStartTime;

    @ApiModelProperty(value = "周期的结束时间.")
    private String periodEndTime;

    @ApiModelProperty(value = "人员集合")
    private List<PropertyScheduleUserModel> userList;

}
