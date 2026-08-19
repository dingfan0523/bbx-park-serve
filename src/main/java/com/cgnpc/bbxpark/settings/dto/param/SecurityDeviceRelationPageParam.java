
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 安全管理员设备关联分页参数模型
 * @author huangyongtao
 * @date 2025/7/31 17:43
 */
@Data
public class SecurityDeviceRelationPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "安全管理id.")
    private Long securityManageId;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

}
