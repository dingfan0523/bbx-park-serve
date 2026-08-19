package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AlarmInfoParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4539052633125946351L;

    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警名称.")
    private String alarmName;

    @ApiModelProperty(value = "告警级别.")
    private String alarmLevel;

    @ApiModelProperty(value = "告警唯一标识(设备DN+规则ID/场景节点ID).")
    private String alarmUnique;

    @Length(max = 30)
    @ApiModelProperty(value = "告警设备DN.")
    private String deviceDn;

    @ApiModelProperty(value = "告警设备名称.")
    private String alarmDevice;

    @ApiModelProperty(value = "告警状态(1待确认、2已确认、3已结束).")
    private Integer alarmStatus;

    @ApiModelProperty(value = "告警确认结果(1真实告警、2系统误报、3忽略告警).")
    private Integer alarmConfirmResult;

    @ApiModelProperty(value = "首次告警时间.")
    private Date alarmFirstTime;

    @ApiModelProperty(value = "末次告警时间.")
    private Date alarmLastTime;

    @ApiModelProperty(value = "告警次数.")
    private Integer alarmCount;

    @ApiModelProperty(value = "告警来源(1规则管理、2场景编排).")
    private Integer alarmSource;

    @Length(max = 255)
    @ApiModelProperty(value = "告警描述.")
    private String alarmDesc;

    @ApiModelProperty(value = "告警结束类型(1设备自动恢复告警2设备停用3系统误报4忽略告警5手动结束).")
    private Integer alarmEndType;

    @Length(max = 255)
    @ApiModelProperty(value = "告警恢复描述.")
    private String alarmRecoveryDesc;
    /**
     * 告警类型
     */
    @ApiModelProperty(value = "告警类型.")
    private String alarmRuleType;

    @ApiModelProperty(value = "id主键集合.")
    private List<Long> ids;

    private Long tenantId;

    @ApiModelProperty(value = "告警结束时间筛选条件的开始时间")
    private Date alarmEndTimeStart;

    @ApiModelProperty(value = "告警结束时间筛选条件的结束时间")
    private Date alarmEndTimeEnd;

    @ApiModelProperty(value = "告警位置id")
    private String spaceAddrId;

    @ApiModelProperty(value = "告警设备id")
    private Long deviceId;

    @ApiModelProperty(value = "告警设备id集合")
    private List<Long> deviceIdList;

    @ApiModelProperty(value = "排序方式")
    private String sort;

    @ApiModelProperty(value = "告警状态(1待确认、2已确认、3已结束).")
    private Integer noAlarmStatus;

}
