
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @value 查看物业排班日历数据模型
 * @author huangyongtao
 * @date 2025/9/28 11:47
 */
@Data
public class PropertyDatePlanModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日期")
    private Date timeDate;


    @ApiModelProperty(value = "物业分组集合")
    private List<PropertyScheduleListModel> propertyScheduleList;

}
