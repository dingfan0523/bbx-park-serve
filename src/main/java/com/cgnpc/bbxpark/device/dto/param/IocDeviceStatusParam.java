
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value ioc设备绑定入参数据模型
 * @author huangyongtao
 * @date 2025/2/21 17:08
 */
@Data
public class IocDeviceStatusParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "状态：0->否;1->是")
    private Integer status;

    @ApiModelProperty(value = "上下线备注")
    private String onlineRemark;

}
