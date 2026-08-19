
package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 物业排班人员分页参数模型
 */
@Data
public class PropertyScheduleUserPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "排班id.")
    private Long scheduleId;

    @ApiModelProperty(value = "是否负责人:1->否;0->是")
    private Integer manager;

    @ApiModelProperty(value = "人员id.")
    private String userId;

    @ApiModelProperty(value = "人员名称.")
    private String userName;

    @ApiModelProperty(value = "人员工号.")
    private String staffid;

    @ApiModelProperty(value = "联系方式.")
    private String phone;

    @ApiModelProperty(value = "部门id.")
    private String departmentId;

    @ApiModelProperty(value = "部门名称.")
    private String departmentName;

}
