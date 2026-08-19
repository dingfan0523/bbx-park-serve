
package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备控制（操作）日志分页参数模型
 */
@Data
public class DeviceOperationLogPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "操作人.")
    private String operator;

    @ApiModelProperty(value = "园区id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建时间.")
    private Date startTime;

    @ApiModelProperty(value = "更新时间.")
    private Date endTime;

    private String userId;

}
