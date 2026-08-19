package com.cgnpc.bbxpark.device.dto.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警忽略配置入参数据模型
 */
@Data
public class AlarmIgnoreConfigParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4548413392303349560L;

//    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警id.")
//    @NotNull( message = "告警id不能为空.")
    private Long alarmInfoId;

    /**
     *告警唯一标识.
     **/
    @ApiModelProperty(value = "告警唯一标识.")
    private String alarmUnique;

    @Length(max = 30)
    @ApiModelProperty(value = "告警设备DN.")
    private String deviceDn;

    @ApiModelProperty(value = "忽略类型(1-本次,2-时间段)")
//    @NotNull( message = "忽略类型不能为空.")
    private String ignoreType;

    @ApiModelProperty(value = "忽略告警起始时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date ignoreStartTime;

    @ApiModelProperty(value = "忽略告警终止时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date ignoreEndTime;


}
