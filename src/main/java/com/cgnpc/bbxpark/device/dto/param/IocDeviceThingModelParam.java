package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;

/***
 * @Description 设别物模型列表入参
 * @author huangyongtao
 * @date 2025/2/26 16:54
 */
@Data
public class IocDeviceThingModelParam implements Serializable {
    @ApiModelProperty(value = "设备id")
    @NotNull(groups = Default.class, message = "设备id不能为空")
    private Long deviceId;
}
