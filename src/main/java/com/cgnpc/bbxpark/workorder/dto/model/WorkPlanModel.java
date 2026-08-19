
package com.cgnpc.bbxpark.workorder.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @value 工单计划管理业务数据模型
 * @author huangyongtao
 * @date 2025/10/23 15:21
 */
@Data
public class WorkPlanModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "计划名称.")
    private String planName;

    @ApiModelProperty(value = "启用状态;1->是;0->否.")
    private Integer status;

    @ApiModelProperty(value = "审核方式;1->是;0->否.")
    private Integer auditType;

    @ApiModelProperty(value = "审核人名称.")
    private String auditUname;

    @ApiModelProperty(value = "审核人id.")
    private String auditUid;

    @ApiModelProperty(value = "审核人工号.")
    private String auditStaffid;

    @ApiModelProperty(value = "物业分组id.")
    private Long scheduleId;

    @ApiModelProperty(value = "物业分组名称.")
    private String scheduleName;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    private Integer dispatchType;

    @ApiModelProperty(value = "处理人名称.")
    private String handleUname;

    @ApiModelProperty(value = "处理人id.")
    private String handleUid;

    @ApiModelProperty(value = "处理人工号.")
    private String handleStaffid;

    @ApiModelProperty(value = "计划的周期;1:周期；2：单次.")
    private Integer planPeriod;

    @ApiModelProperty(value = "周期类型;year：年；quarter：季度；month：月；week：周；day：日.")
    private String periodType;

    @ApiModelProperty(value = "周期的标识;1:第一天；2：最后一天.")
    private Integer periodSign;

    @ApiModelProperty(value = "周期的开始时间.")
    private Date periodStartTime;

    @ApiModelProperty(value = "计划开始的时间.")
    private Date planStartTime;

    @ApiModelProperty(value = "计划时长;单位小时.")
    private Integer planDuration;

    @ApiModelProperty(value = "计划描述.")
    private String remark;

    @ApiModelProperty(value = "创建人工号")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "工单类型.")
    private String planType;

    @ApiModelProperty(value = "工单来源.")
    private String planSource;

    @ApiModelProperty(value = "工单编码.")
    private String planCode;

    @ApiModelProperty(value = "巡检点id;多个以英文逗号隔开.")
    private String pointId;

    @ApiModelProperty(value = "巡更路线id;多个以英文逗号隔开.")
    private String routeId;

    @ApiModelProperty(value = "空间位置id;多个以英文，逗号隔开.")
    private String spaceId;

    @ApiModelProperty(value = "抄表要求.")
    private String readingRemark;

    @ApiModelProperty(value = "抄表类型;water：水表；electricity：电表；gas：燃气表..")
    private String readingType;
}
