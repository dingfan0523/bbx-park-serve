package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AlarmHandleRecordParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4966796494357652777L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "设备id(bbx_device表的device_id字段).")
    private String deviceId;

    @ApiModelProperty(value = "告警主键id.")
    @NotNull( message = "告警id不能为空")
    private Long alarmId;

    @ApiModelProperty(value = "环节(1生成告警2告警确认3告警级别调整).")
    private Integer link;

    @ApiModelProperty(value = "告警确认结果(1真实告警、2误报、3运维导致).")
    @NotNull( message = "告警确认结果不能为空")
    private Integer alarmConfirmResult;

    @ApiModelProperty(value = "告警状态(1待确认、2已确认、3已结束).")
    private Integer alarmStatus;

    @ApiModelProperty(value = "告警来源(1规则管理、2场景编排).")
    private Integer alarmSource;

    @Length(max = 255)
    @ApiModelProperty(value = "操作人.")
    private String operator;

    @ApiModelProperty(value = "操作时间.")
    private Date operateTime;

    @Length(max = 255)
    @ApiModelProperty(value = "操作备注.")
    private String operateDesc;
    @ApiModelProperty(value = "是否转工单.Y-是；N-否")
    private String work;
//    @ApiModelProperty(value = "工单处理人")
//    private Long workHandlerId;

    @ApiModelProperty(value = "id主键集合.")
    private List<Long> ids;

    @ApiModelProperty(value = "设备id主键集合.")
    private List<Long> deviceIds;

    @ApiModelProperty(value = "告警级别")
    private String alarmLevel;



    @ApiModelProperty(value = "审核方式;0->否;1->是.")
    @NotNull(message = "审核方式不能为空")
    private Integer auditType = 1;

    @ApiModelProperty(value = "审核人id.")
    private String auditUid;

    @Length(max = 50)
    @ApiModelProperty(value = "审核人名称.")
    private String auditUname;

    @Length(max = 50)
    @ApiModelProperty(value = "审核人工号.")
    private String auditStaffid;

    @ApiModelProperty(value = "物业分组id.")
    //@NotNull(message = "物业分组id不能为空")
    private Long scheduleId;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    //@NotNull( message = "派单方式不能为空")
    private Integer dispatchType;

    @ApiModelProperty(value = "处理人id.")
    private String handleUid;

    @Length(max = 50)
    @ApiModelProperty(value = "处理人名称.")
    private String handleUname;

    @Length(max = 50)
    @ApiModelProperty(value = "处理人工号.")
    private String handleStaffid;
}
