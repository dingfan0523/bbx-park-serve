
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 物业排班业务数据模型
 */
@Data
public class PropertyScheduleModel implements Serializable {

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

    @ApiModelProperty(value = "人员集合")
    private List<PropertyScheduleUserModel> userList;
}
