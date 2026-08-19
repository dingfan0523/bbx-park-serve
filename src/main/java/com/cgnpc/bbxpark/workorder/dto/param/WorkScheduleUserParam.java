
package com.cgnpc.bbxpark.workorder.dto.param;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 工单排班人员入参数据模型
 * @author huangyongtao
 * @date 2025/11/4 16:52
 */
@Data
public class WorkScheduleUserParam implements Serializable {
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

    @Length(max = 50)
    @ApiModelProperty(value = "物业分组名称.")
    private String scheduleName;

    @ApiModelProperty(value = "人员id.")
    private String userId;

    @ApiModelProperty(value = "是否负责人:0->是;1->否.")
    private Integer manager;

    @Length(max = 50)
    @ApiModelProperty(value = "人员名称.")
    private String userName;

    @Length(max = 50)
    @ApiModelProperty(value = "人员工号.")
    private String staffid;

    @Length(max = 50)
    @ApiModelProperty(value = "联系方式.")
    private String phone;

    @ApiModelProperty(value = "部门id.")
    private String departmentId;

    @Length(max = 64)
    @ApiModelProperty(value = "部门名称.")
    private String departmentName;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人id.")
    private String updatorId;

    @ApiModelProperty(value = "更新人姓名.")
    private String updateBy;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;
}
