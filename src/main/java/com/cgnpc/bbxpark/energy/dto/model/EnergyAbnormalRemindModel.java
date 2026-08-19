package com.cgnpc.bbxpark.energy.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @create zhaoshuo
 * @time 2025/4/23
 * @desc 能源异常提醒model
 */
@Data
public class EnergyAbnormalRemindModel  implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 提醒标题 */
    @ApiModelProperty(value = "提醒标题")
    private String remindName ;
    /** 提醒内容 */
    @ApiModelProperty(value = "提醒内容")
    private String remindContent ;
    /** 报单状态（0未报单1已报单） */
    @ApiModelProperty(value = "报单状态（0未报单1已报单）")
    private Integer status ;
    /** 报单业务id */
    @ApiModelProperty(value = "报单业务id")
    private Long businessId ;
    /** 设备id */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    /** 设备名称 */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /** 空间id */
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    /** 空间名称 */
    @ApiModelProperty(value = "空间名称")
    private String spaceName;
    @ApiModelProperty(value = "创建时间-提醒时间")
    private Date createTime;
}
