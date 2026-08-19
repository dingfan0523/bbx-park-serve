
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @value 安全管理员设备关联入参数据模型
 * @author huangyongtao
 * @date 2025/8/1 10:54
 */
@Data
public class SecurityDeviceRelationParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "安全管理id.")
    private Long securityManageId;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备id集合.")
    private List<Long> deviceIds;

}
