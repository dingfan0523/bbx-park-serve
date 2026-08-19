
package com.cgnpc.bbxpark.workorder.dto.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 工单计划详细信息列表参数模型
 * @author huangyongtao
 * @date 2025/3/25 16:17
 */
@Data
public class WorkPlanDetailListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "计划id.")
    private Long planId;

    @ApiModelProperty(value = "计划类型（抄表计划：meterPlan；维保计划：maintainPlan；巡检计划：inspectionPlan；巡更计划：patrolPlan；盘点计划：inventoryPlan；任务计划：taskPlan）.")
    private String planType;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "计划名称.")
    private String planName;

    @ApiModelProperty(value = "空间位置id;多个以英文，逗号隔开.")
    private String spaceId;

    @ApiModelProperty(value = "空间位置名称.")
    private String spaceName;

    @ApiModelProperty(value = "抄表类型;water：水表；electricity：电表；gas：燃气表.")
    private String readingType;

    @ApiModelProperty(value = "计划的周期;1:周期抄表；2：单次抄表.")
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

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
