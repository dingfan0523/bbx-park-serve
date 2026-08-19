
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 安全管理员设备关联列表参数模型
 * @author huangyongtao
 * @date 2025/7/31 17:42
 */
@Data
public class SecurityDeviceRelationListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "安全管理id.")
    private Long securityManageId;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;


}
