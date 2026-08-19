
package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备标签分页参数模型
 */
@Data
public class DeviceLabelPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "标签编码.")
    private String labelCode;

    @ApiModelProperty(value = "标签名称.")
    private String labelName;

    @ApiModelProperty(value = "标签颜色.")
    private String labelColour;

    @ApiModelProperty(value = "标签描述.")
    private String labelDescribe;

    @ApiModelProperty(value = "租户号.")
    private Long tenantId;

}
