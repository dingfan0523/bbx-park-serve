
package com.cgnpc.bbxpark.workorder.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 工单排班人员业务数据模型
 * @author huangyongtao
 * @date 2025/11/4 16:59
 */
@Data
public class WorkScheduleUserModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "物业分组id.")
    private Long scheduleId;

    @ApiModelProperty(value = "物业分组名称.")
    private String scheduleName;


    @ApiModelProperty(value = "是否负责人:1->是;0->否.")
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
