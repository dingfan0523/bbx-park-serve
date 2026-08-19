
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 设备分组关系列表参数模型
 * @author huangyongtao
 * @date 2024/8/12 11:53
 */
@Data
public class DeviceGroupRelListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关系id.")
    private Long id;

    @ApiModelProperty(value = "分组id.")
    private Long groupId;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "园区id.")
    private Long tenantId;

}
