
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class StationModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4557652001901646177L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "人员id.")
    private String userId;

    @ApiModelProperty(value = "人员名称.")
    private String userName;

    @ApiModelProperty(value = "人员工号.")
    private String staffid;

    @ApiModelProperty(value = "部门id.")
    private String departmentId;

    @ApiModelProperty(value = "部门名称.")
    private String departmentName;

    @ApiModelProperty(value = "分配人id.")
    private String assignerId;

    @ApiModelProperty(value = "分配人名称.")
    private String assignerName;

    @ApiModelProperty(value = "分配人工号.")
    private String assignerStaffid;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;
}
