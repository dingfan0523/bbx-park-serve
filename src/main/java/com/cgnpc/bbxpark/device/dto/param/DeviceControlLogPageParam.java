
package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class DeviceControlLogPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3708053177092254659L;

    private String id;

    @ApiModelProperty(value = "姓名.")
    private String name;

    @ApiModelProperty(value = "设备id.")
    private String deviceId;

    @ApiModelProperty(value = "渠道.")
    private String channel;

    @ApiModelProperty(value = "结果.")
    private String result;

    @ApiModelProperty(value = "开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "结束时间.")
    private Date endTime;
}
