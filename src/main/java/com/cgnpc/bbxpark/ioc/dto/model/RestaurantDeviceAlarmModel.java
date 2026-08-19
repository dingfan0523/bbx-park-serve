package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 大屏餐厅设备告警模型
 */
@Data
public class RestaurantDeviceAlarmModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备位置.")
    private String spaceName;

    @ApiModelProperty(value = "告警图片")
    private String imageUrl;

    @ApiModelProperty(value = "告警名称")
    private String alarmName;

}

