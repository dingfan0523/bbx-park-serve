
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @value ioc设备绑定入参数据模型
 * @author huangyongtao
 * @date 2025/2/21 17:08
 */
@Data
public class IocDeviceBindingParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id集合")
    private List<Long> idList;

    @ApiModelProperty(value = "所属空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "产品id.")
    private Long productId;

    @ApiModelProperty(value = "所属部门id.")
    private String departmentId;

    @ApiModelProperty(value = "所属部门名称.")
    private String departmentName;

}
