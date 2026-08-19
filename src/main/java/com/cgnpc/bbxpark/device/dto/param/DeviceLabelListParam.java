
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 设备标签列表参数模型
 */
@Data
public class DeviceLabelListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "标签编码.")
    private String labelCode;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "标签名称.")
    private String labelName;

    @ApiModelProperty(value = "标签颜色.")
    private String labelColour;

    @ApiModelProperty(value = "标签描述.")
    private String labelDescribe;

    @ApiModelProperty(value = "租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "id集合")
    private List<Long> ids;

    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIdList;

    @ApiModelProperty(value = "设备标签id(存在)")
    private Long labelId;
}
