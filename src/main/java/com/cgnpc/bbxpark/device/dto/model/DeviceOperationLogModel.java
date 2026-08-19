
package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备控制（操作）日志业务数据模型
 */
@Data
public class DeviceOperationLogModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "设备id.")
    private String deviceId;

    @ApiModelProperty(value = "类型(1规则,2场景,3能力清单).")
    private Long type;

    @ApiModelProperty(value = "控制报文.")
    private String params;

    @ApiModelProperty(value = "返回报文.")
    private String response;

    private String createUserName;

    private String deviceName;

    private Long spaceId;

    private String spaceName;

    private Date createTime;

}
