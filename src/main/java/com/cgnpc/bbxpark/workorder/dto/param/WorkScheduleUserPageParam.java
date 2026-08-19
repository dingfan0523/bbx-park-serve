
package com.cgnpc.bbxpark.workorder.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 工单排班人员分页参数模型
 * @author huangyongtao
 * @date 2025/11/4 16:51
 */
@Data
public class WorkScheduleUserPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "物业分组id.")
    private Long scheduleId;

    @ApiModelProperty(value = "物业分组名称.")
    private String scheduleName;

    @ApiModelProperty(value = "人员id.")
    private String userId;

    @ApiModelProperty(value = "是否负责人:0->是;1->否.")
    private Integer manager;

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
